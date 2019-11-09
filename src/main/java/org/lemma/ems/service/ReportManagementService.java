package org.lemma.ems.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.DeviceReportMasterDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceReportMasterDTO;
import org.lemma.ems.base.dao.dto.ReportDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.reports.core.ReportConstants;
import org.lemma.ems.reports.core.ReportConstants.Reports;
import org.lemma.ems.ui.model.ReportManagementForm;
import org.lemma.ems.ui.model.ReportManagementForm.ReportStatus;
import org.lemma.ems.util.BitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("reportManagementService")
public class ReportManagementService {

	private static final Logger logger = LoggerFactory.getLogger(ReportManagementService.class);

	@Autowired
	private DeviceDetailsDAO deviceDetailsDAO;
	
	@Autowired
	private DeviceReportMasterDAO deviceReportMasterDAO;

	@Autowired
	ReloadableResourceBundleMessageSource msgSource;
	
	@Autowired
	Sender sender;

	/**
	 * @return
	 */
	public ModelAndView showReportManagementPage() {
		ModelAndView modelAndView = new ModelAndView("reports/manage");
		
		List<ReportDTO> list = new ArrayList<>();
		
		for(Reports type : ReportConstants.Reports.values()) {
			if(type != Reports.SUMMARY_RPT && type !=  Reports.ELOBORATE_RPT)
				list.add(new ReportDTO(type.getDesc(), type.getType()));
		}
				
		List<DeviceDetailsDTO> emsActiveDevices = deviceDetailsDAO.fetchDevice4ReportMaster();
		
		modelAndView.addObject("reportManagementForm", new ReportManagementForm());
		modelAndView.addObject("reportTypes", list);
		modelAndView.addObject("activeDevices", emsActiveDevices);

		return modelAndView;
	}
	
	/**
	 * @param form
	 * @return
	 */
	public ModelAndView updateReportMaster(ReportManagementForm form) {
		
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/reports/management");
		modelAndView.addObject("msg", msgSource.getMessage("reportmaster.update.success", null, Locale.getDefault()));
		long timeStamp = System.currentTimeMillis();
		
		long[] uniqueId = form.getUniqueId();
		List<ReportStatus> config = form.getConfig();
		
		logger.trace("Requested to update DeviceReport details");
		
		try {
			for(int i = 0;i<uniqueId.length;i++) {
				DeviceReportMasterDTO dto = new DeviceReportMasterDTO();
				dto.setDeviceid(uniqueId[i]);
				ReportStatus status = config.get(i);
				int type = createReportType(status);
				dto.setType(type);
				dto.setStatus(type);
				dto.setCreatedtimestamp(timeStamp);
				
				int updatedRecord = deviceReportMasterDAO.updateDeviceReport(dto);
				
				if(updatedRecord <= 0)
					deviceReportMasterDAO.insertDeviceReport(dto);
			}
			
			logger.debug("DeviceReport details updated successfully");
			
		} catch (Exception e) {
			logger.error("Failed to update report master {}", e);
			modelAndView.addObject("msg", msgSource.getMessage("reportmaster.update.failure", null, Locale.getDefault()));
		}
		
		return modelAndView;
	}
	
	/**
	 * @param dto
	 */
	public int createReportType(ReportStatus dto) {
		boolean[] status = dto.getStatus();
		int[] bitPosition = dto.getBitPosition();
		long sum = 0;
		
		//When status is null, set total is 0
		if(status == null)
			return (int)sum;
		
		//When specific positions status is true, accumulate it
		int index = 0;
		for(int position : bitPosition) {
			if(index < status.length && status[index++])
				sum = BitUtil.setBit(sum, position);
		}
		
		return (int)sum;
	}
}
