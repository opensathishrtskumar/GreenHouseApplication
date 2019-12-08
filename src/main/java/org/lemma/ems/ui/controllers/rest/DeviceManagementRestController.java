
package org.lemma.ems.ui.controllers.rest;

import java.io.IOException;

import org.lemma.ems.base.core.util.DeviceConnectionValidator;
import org.lemma.ems.reports.DailyCumulativeReport;
import org.lemma.ems.ui.model.DeviceConnectionResponse;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.util.RequestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RTS Sathish  Kumar
 *
 */
@RestController
@RequestMapping("/devicemanagement")
public class DeviceManagementRestController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementRestController.class);
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	DailyCumulativeReport dailyCumulativeReport;

	@RequestMapping(value = "/connection/test", method = RequestMethod.POST)
	@ResponseBody
	public DeviceConnectionResponse testDeviceConnection(@RequestBody DeviceDetailsForm request) {

		logger.debug("Testing connection request {}", request);
		
		DeviceConnectionResponse response = new DeviceConnectionResponse();
		DeviceConnectionValidator validator  = new DeviceConnectionValidator();
		
		try {
			validator.validateForm(request);
		} catch (Exception e) {
			RequestResponseUtil.generateFailureResponse(response, e);
			return response;
		}
		
		// FIXME : test connection of incoming device and respond accordingly
		RequestResponseUtil.generateSuccessResponse(response);

		return response;
	}

	
	@RequestMapping(value = "/reports/testreport", method = RequestMethod.GET)
	public String fetchTestRecords() {
		try {
			dailyCumulativeReport.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Success";
	}
}
