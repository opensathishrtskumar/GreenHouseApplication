package org.lemma.ems.ui.controllers;

import org.lemma.ems.service.DeviceManagementService;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Controller
public class DeviceManagementController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementController.class);

	@Autowired
	DeviceManagementService deviceManagementService;

	/**
	 * @return ModelAndView for Device Management page
	 */
	@RequestMapping(value = "/ems/devices/show", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return deviceManagementService.showReportsPage();
	}

	/**
	 * @return ModelAndView for Device Management page
	 */
	@RequestMapping(value = "/ems/device/add", method = RequestMethod.POST)
	public ModelAndView checkDeviceConnection(@ModelAttribute("deviceDetailsForm") DeviceDetailsForm request) {

		logger.debug("Device details add request {}", request);

		return deviceManagementService.addDevice(request);
	}
}
