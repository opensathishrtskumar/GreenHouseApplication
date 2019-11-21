package org.lemma.ems.ui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Controller
@RequestMapping("/docs")
public class DocumentsController {

	private static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);

	/**
	 * @return
	 */
	@RequestMapping(value = "/mainpage/show", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("docsmain");
	}
}
