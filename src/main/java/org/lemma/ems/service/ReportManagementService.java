package org.lemma.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.ReportDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.reports.core.ReportConstants;
import org.lemma.ems.reports.core.ReportConstants.Reports;
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
				
		List<DeviceDetailsDTO> emsActiveDevices = deviceDetailsDAO.fetchEMSActiveDevices();
		
		modelAndView.addObject("reportTypes", list);
		modelAndView.addObject("activeDevices", emsActiveDevices);


		return modelAndView;
	}
}
