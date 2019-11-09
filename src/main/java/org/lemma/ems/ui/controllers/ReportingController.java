package org.lemma.ems.ui.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.lemma.ems.service.ReportManagementService;
import org.lemma.ems.ui.model.DateRangeReportForm;
import org.lemma.ems.ui.model.ReportManagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportingController {

	private static final Logger logger = LoggerFactory.getLogger(ReportingController.class);

	@Autowired
	private ReportManagementService reportService;

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("reports");
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/management", method = RequestMethod.GET)
	public ModelAndView showManageReports() {
		return reportService.showReportManagementPage();
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/management", method = RequestMethod.POST)
	public ModelAndView updateReportsMaster(@ModelAttribute("reportManagementForm") ReportManagementForm form) {
		return reportService.updateReportMaster(form);
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.GET)
	public ModelAndView getDateRangeReportsPage() {
		return reportService.getDateRageReportPage();
	}

	/**
	 * @param form
	 * @param formBinding
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.POST)
	public void postDateRangeReportsPage(@Valid DateRangeReportForm form, BindingResult formBinding,
			HttpServletResponse response) throws Exception {

		if (formBinding.hasErrors()) {
			logger.error("Form has error !!!");
		}
	}
}
