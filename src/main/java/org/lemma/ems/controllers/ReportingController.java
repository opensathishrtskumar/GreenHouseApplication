package org.lemma.ems.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.lemma.ems.model.DateRangeReportForm;
import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportingController {

	private static final Logger logger = LoggerFactory.getLogger(ReportingController.class);


	@RequestMapping(value = "/ems/reports", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("reports");
	}

	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.GET)
	public ModelAndView getDateRangeReportsPage() {
		ModelAndView view = new ModelAndView("reports/daterange", "reportForm", new DateRangeReportForm());
		return view;
	}

	@RequestMapping(value = "/ems/reports/getmemorymapping/{deviceid}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody Map<String, String> getMemoryMappings(@PathVariable("deviceid") int deviceid) {
		logger.trace("Memory mapping requested for deviceuniqueid {}", deviceid);

		Map<String, String> memoryMapping = new LinkedHashMap<>();

		return memoryMapping;
	}

	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.POST)
	public void postDateRangeReportsPage(@Valid DateRangeReportForm form, BindingResult formBinding,
			HttpServletResponse response) throws Exception {

		logger.debug("DateRange report requested with input {}", EMSUtility.convertObjectToJSONString(form));

		if (formBinding.hasErrors()) {
			logger.error("Form has error !!!");
		}
	}
}
