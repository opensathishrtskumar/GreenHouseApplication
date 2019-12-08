package org.lemma.ems.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.joda.time.LocalDate;
import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.constants.Core.MemoryMapping;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.DeviceReportMasterDAO;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceReportMasterDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.dao.dto.ReportDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.reports.core.ReportConstants;
import org.lemma.ems.reports.core.ReportConstants.Reports;
import org.lemma.ems.ui.model.DateRangeReportForm;
import org.lemma.ems.ui.model.ReportManagementForm;
import org.lemma.ems.ui.model.ReportManagementForm.ReportStatus;
import org.lemma.ems.util.BitUtil;
import org.lemma.ems.util.DateUtil;
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
	@Autowired
	PollingDetailsDAO pollingDetailsDAO;

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
	
	/**
	 * @return
	 */
	public ModelAndView getDateRageReportPage() {
		ModelAndView modelAndView = new ModelAndView("reports/daterange", "reportForm", new DateRangeReportForm());
		
		List<DeviceDetailsDTO> deviceList = deviceDetailsDAO.fetchEMSActiveDevices();
		MemoryMapping[] source = Core.MemoryMapping.values();
		//Ignore EOM from display
		MemoryMapping[] target = new MemoryMapping[source.length - 1];
		
		System.arraycopy(source, 0, target, 0, target.length);
		
		modelAndView.addObject("deviceList", deviceList);
		modelAndView.addObject("memoryMapping", target);
		
		return modelAndView;
	}
	
	
	public ModelAndView insertDummy() {
		ModelAndView modelAndView = new ModelAndView("reports/daterange", "reportForm", new DateRangeReportForm());
		LocalDate plusDays = LocalDate.now().plusDays(-1);
		Date date = plusDays.toDate();
		
		long startOfDay = DateUtil.getStartOfDay(date);
		long endOfDay = DateUtil.getEndOfDay(date);
		
		float voltage = 1.000f;
		float w1 = 1.000f;
		float va1 = 1.000f;
		float factor = 0.012f;
		Random rand = new Random();
		
		for(long i = startOfDay;i < endOfDay;i = i + 200000) {
			//Random number to include random seconds in PolledOn Time
			int randInt = rand.nextInt(60000); 
			PollingDetailsDTO pollingDetailsDTO = new PollingDetailsDTO();
			pollingDetailsDTO.setUniqueId(2);
			pollingDetailsDTO.setPolledOn(i + randInt);
			pollingDetailsDTO.setVoltage_br(voltage += factor);
			pollingDetailsDTO.setW1(w1 += factor);
			pollingDetailsDTO.setVa1(va1 += factor);
			
			pollingDetailsDAO.insertPollingDetails(pollingDetailsDTO);
		}
		
		return modelAndView;
	}

}
