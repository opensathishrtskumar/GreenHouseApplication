package org.lemma.ems.ui.controllers;

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

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/management", method = RequestMethod.GET)
	public ModelAndView managementView() {
		return new ModelAndView("management");
	}
}
