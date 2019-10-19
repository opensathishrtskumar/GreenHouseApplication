package org.lemma.ems.service;

import java.util.List;
import java.util.Locale;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.model.DeviceFormDetails;
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
		/**
		 * On successfull insertion
		 * 	1. publish reload event
		 *  2. Redirect back to show devices page with success 
		 */
		//DeviceDetailsForm to DeviceDetailsDTO convertion and insert it
		
		
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/devices/show");
		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("device.added", null, Locale.getDefault()));
		return modelAndView;
	}

}
