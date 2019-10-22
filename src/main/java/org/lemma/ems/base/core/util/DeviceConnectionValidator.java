package org.lemma.ems.base.core.util;

import java.util.List;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author RTS Sathish Kumar Validates memory mapping detail
 * 
 *         Used only in RestController, having all detailed validation
 */
public class DeviceConnectionValidator {

	private static final Logger logger = LoggerFactory.getLogger(DeviceConnectionValidator.class);

	public DeviceConnectionValidator() {
		// NOOP
	}

	/**
	 * 
	 * {@link Core.MemoryMapping}
	 * 
	 * @param device
	 * @return
	 * @throws Exception
	 */
	public boolean validateForm(DeviceDetailsForm device) throws Exception {

		List<DeviceMemoryDTO> memoryMappings = null;

		if (device == null || (memoryMappings = device.getMemoryMappings()) == null || memoryMappings.isEmpty())
			throw new Exception("Invalid DeviceDetails Found for validation");

		/**
		 * 1. Device name should have name 2. Device ID can be between 1 and 255 since
		 * modbus connector support 0-255, 0 for broadcast message
		 *
		 */
		if (StringUtils.isEmpty(device.getDeviceName()) || (device.getDeviceId() < 1 || device.getDeviceId() > 255)) {
			throw new Exception("Invalid Device Name or Device ID");
		}

		for (DeviceMemoryDTO memoryMapping : memoryMappings) {
			MemoryMappingParser.parseAndValidateMappings(memoryMapping.getMemoryMapping());
		}

		return true;
	}

}
