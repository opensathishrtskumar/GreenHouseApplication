package org.lemma.ems.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lemma.ems.service.DeviceManagementService;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.validator.DeviceDetailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Controller()
public class DeviceManagementController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementController.class);

	@Autowired
	DeviceManagementService deviceManagementService;

	/**
	 * @return ModelAndView for Device Management page
	 */
	@RequestMapping(value = "/ems/devices/show", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return deviceManagementService.showDeviceReportsPage();
	}

	/**
	 * @return ModelAndView for Device Management page
	 */
	@RequestMapping(value = "/ems/device/add", method = RequestMethod.POST)
	public ModelAndView addNewDevice(@ModelAttribute("deviceDetailsForm") DeviceDetailsForm form,
			BindingResult formBinding, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.debug("Device details add request {}", form);

		ModelAndView modelAndView = deviceManagementService.showDeviceReportsPage();

		DeviceDetailValidator validator = new DeviceDetailValidator(form, formBinding);
		validator.validateDeviceDetailsForm();

		// Return errors if there is any, along with memoryMappings to iterate and Form
		// Binding
		if (formBinding.hasErrors()) {
			modelAndView.addObject("deviceDetailsForm", form);
			modelAndView.addObject("memoryMappings", form.getMemoryMappings());
			return modelAndView;
		}

		return deviceManagementService.addNewDevice(form);
	}

	/**
	 * @return ModelAndView for Device Management page
	 */
	@RequestMapping(value = "/ems/device/update", method = RequestMethod.POST)
	public ModelAndView updateExistingDevice(@ModelAttribute("deviceDetailsForm") DeviceDetailsForm form) {
		return deviceManagementService.updateExistingDevice(form);
	}
}
