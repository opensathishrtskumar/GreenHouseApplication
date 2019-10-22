package org.lemma.ems.service;

import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.ui.model.DeviceDetailsForm;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DeviceMapper {

	/**
	 * @param form
	 * @return
	 */
	public static DeviceDetailsDTO mapForm2Dto(DeviceDetailsForm form) {
		DeviceDetailsDTO dto = new DeviceDetailsDTO();

		dto.setUniqueId(form.getUniqueId());
		dto.setDeviceId(form.getDeviceId());
		dto.setDeviceName(form.getDeviceName());
		dto.setBaudRate(form.getBaudRate());
		dto.setMethod(form.getMethod());
		dto.setParity(form.getParity());
		dto.setPort(form.getPort());
		dto.setRegisterMapping(form.getRegisterMapping());
		dto.setStatus(form.getStatus());
		dto.setStopbit(form.getStopbit());
		dto.setType(form.getType());
		dto.setWordLength(form.getWordLength());
		dto.setMemoryMappings(form.getMemoryMappings());
		dto.setCreatedTimeStamp(form.getCreatedTimeStamp());
		dto.setModifiedTimeStamp(form.getModifiedTimeStamp());
		// TODO: calculate hashkey
		dto.setHashKey("HASHKEY");

		return dto;
	}

}
