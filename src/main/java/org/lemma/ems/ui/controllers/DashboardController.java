package org.lemma.ems.ui.controllers;

import org.lemma.ems.service.DeviceManagementService;
import org.lemma.ems.ui.model.DateRangeReportForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Controller
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private DeviceManagementService deviceManagementService;
	
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/dashboard/show", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("dashboard");
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/dashboard/chartview", method = RequestMethod.GET)
	public ModelAndView getDateRangeReportsPage() {
		return new ModelAndView("dashboard/chartview", "reportForm", new DateRangeReportForm());
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/dashboard/management/show", method = RequestMethod.GET)
	public ModelAndView showDashboardManagement() {
		return deviceManagementService.showDashboardMngmtView();
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/dashboardmngmt/update", method = RequestMethod.GET)
	public ModelAndView updateDashboardManagement() {
		return deviceManagementService.showDashboardMngmtView();
	}
}
