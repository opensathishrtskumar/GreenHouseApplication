package org.lemma.ems.service;

import java.util.List;
import java.util.Locale;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.DeviceMemoryDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.ui.controllers.DeviceManagementController;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.model.DeviceFormDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("deviceManagementService")
public class DeviceManagementService {

	private static final Logger logger = LoggerFactory.getLogger(DeviceManagementService.class);

	@Autowired
	private DeviceDetailsDAO deviceDetailsDAO;

	@Autowired
	ReloadableResourceBundleMessageSource msgSource;

	/**
	 * @return
	 */
	public ModelAndView showReportsPage() {
		ModelAndView modelAndView = new ModelAndView("devicelist");

		// List of devices not DELETED and Type EMS
		List<DeviceDetailsDTO> deviceList = deviceDetailsDAO.fetchDeviceDetails(DeviceDetailsDAO.RETRIEVE_EMS_DEVICES,
				new Object[] { DeviceDetailsDAO.Status.DELETED.getStatus(), DeviceDetailsDAO.Type.EMS.getType() });

		// Holds existing device details
		modelAndView.addObject("existingDeviceList", deviceList);
		// Form values like parity list and so on
		modelAndView.addObject("formDetails", new DeviceFormDetails());

		// Holds new device information
		modelAndView.addObject("deviceDetailsForm", new DeviceDetailsForm());

		return modelAndView;
	}

	/**
	 * @param form
	 * @return
	 */
	public ModelAndView addNewDevice(DeviceDetailsForm form) {
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/devices/show");
		modelAndView.addObject("msg", msgSource.getMessage("device.added", null, Locale.getDefault()));

		/**
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		long timeStamp = System.currentTimeMillis();
		form.setCreatedTimeStamp(timeStamp);
		form.setModifiedTimeStamp(timeStamp);
		form.setStatus(form.isEnabled() ? DeviceDetailsDAO.Status.ACTIVE.getStatus()
				: DeviceDetailsDAO.Status.DISABLED.getStatus());
		form.setType(DeviceDetailsDAO.Type.EMS.getType());

		for (DeviceMemoryDTO memory : form.getMemoryMappings()) {
			memory.setCreatedTimeStamp(timeStamp);
			memory.setStatus(DeviceMemoryDAO.Status.ACTIVE.getStatus());
		}

		DeviceDetailsDTO dto = DeviceMapper.mapForm2Dto(form);

		try {
			long insertDeviceDetails = deviceDetailsDAO.insertDeviceDetails(dto);
		} catch (Exception e) {
			logger.error("Failed to insert new device {}", e);
			modelAndView.addObject("msg", msgSource.getMessage("device.added.error", null, Locale.getDefault()));
		}

		return modelAndView;
	}

}
