package org.lemma.ems.service;

import java.util.List;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	 * @return
	 */
	public ModelAndView showReportsPage() {
		ModelAndView modelAndView = new ModelAndView("devicelist");

		// List of devices not DELETED and Type EMS
		List<DeviceDetailsDTO> deviceList = deviceDetailsDAO.fetchDeviceDetails(DeviceDetailsDAO.RETRIEVE_EMS_DEVICES,
				new Object[] { DeviceDetailsDAO.Status.DELETED.getStatus(), DeviceDetailsDAO.Type.BMS.getType() });

		modelAndView.addObject("deviceList", deviceList);
		return modelAndView;
	}

}
