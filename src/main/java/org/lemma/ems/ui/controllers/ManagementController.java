package org.lemma.ems.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagementController {

	private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

	@Autowired
	private Environment environment;

	@RequestMapping(value = "/ems/homeSignedIn", method = RequestMethod.GET)
	public ModelAndView showHomeSignedIn(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("homeSignedIn");
	}

	@RequestMapping(value = "/ems/management", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("management");
	}
}
