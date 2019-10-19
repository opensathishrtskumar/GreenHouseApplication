
package org.lemma.ems.ui.controllers.rest;

import org.lemma.ems.ui.model.DeviceConnectionResponse;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.util.RequestResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/devicemanagement")
public class DeviceManagementRestController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementRestController.class);

	@RequestMapping(value = "/connection/test", method = RequestMethod.POST)
	@ResponseBody
	public DeviceConnectionResponse testDeviceConnection(@RequestBody DeviceDetailsForm request) {

		logger.debug("Testing connection request {}", request);

		DeviceConnectionResponse response = new DeviceConnectionResponse();
		// FIXME : test connection of incoming device

		RequestResponseUtil.generateSuccessResponse(response);

		return response;
	}

}
