package org.lemma.ems.UI.controllers.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HelperController {

	private static final Logger logger = LoggerFactory.getLogger(HelperController.class);

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	@ResponseBody
	public String showReportsPage() {
		return "Success";
	}
}
