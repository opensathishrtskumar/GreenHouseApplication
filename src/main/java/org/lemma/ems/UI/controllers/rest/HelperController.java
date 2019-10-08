package org.lemma.ems.UI.controllers.rest;

import org.lemma.ems.base.mqueue.publisher.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HelperController {

	private static final Logger logger = LoggerFactory.getLogger(HelperController.class);
	
	
	@Autowired
	Sender sender;
	
	@Value("${startup.notification}")
	private String startupNotification;
	
	
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	@ResponseBody
	public String showReportsPage() {
		
		sender.publishEvent(startupNotification, "SUCCESS");
		
		return "Success";
	}
}
