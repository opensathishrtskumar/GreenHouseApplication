package org.lemma.ems.UI.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.lemma.ems.UI.model.DateRangeReportForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);


	@RequestMapping(value = "/ems/dashboard/show", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("dashboard");
	}

	@RequestMapping(value = "/ems/dashboard/chartview", method = RequestMethod.GET)
	public ModelAndView getDateRangeReportsPage() {
		ModelAndView view = new ModelAndView("dashboard/chartview", "reportForm", new DateRangeReportForm());
		return view;
	}

	@RequestMapping(value = "/ems/dashboard/getmemorymapping/{deviceid}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody Map<String, String> getMemoryMappings(@PathVariable("deviceid") int deviceid) {
		logger.trace("Memory mapping requested for deviceuniqueid {}", deviceid);


		Map<String, String> memoryMapping = new LinkedHashMap<>();


		return memoryMapping;
	}

}
