package org.lemma.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
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
		dto.setStopbit(form.getStopbit());
		dto.setType(form.getType());
		dto.setEncoding(form.getEncoding());
		dto.setWordLength(form.getWordLength());
		dto.setMemoryMappings(form.getMemoryMappings());
		dto.setStatus(form.getStatus());
		dto.setCreatedTimeStamp(form.getCreatedTimeStamp());
		dto.setModifiedTimeStamp(form.getModifiedTimeStamp());
		// TODO: calculate hashkey
		dto.setHashKey("HASHKEY");

		return dto;
	}

	public static List<ExtendedSerialParameter> mapDevicesToSerialParams(List<DeviceDetailsDTO> devices) {
		List<ExtendedSerialParameter> paramList = new ArrayList<>();

		for (DeviceDetailsDTO device : devices) {
			paramList.add(mapDeviceDetailToExtSerialParam(device));
		}

		return paramList;
	}

	public static ExtendedSerialParameter mapDeviceDetailToExtSerialParam(DeviceDetailsDTO devices) {

		ExtendedSerialParameter parameters = new ExtendedSerialParameter(devices.getPort(), devices.getBaudRate(), 0, 0,
				devices.getWordLength(), devices.getStopbit(), 0);
		
		parameters.setUniqueId(devices.getUniqueId());
		parameters.setDeviceName(devices.getDeviceName());
		parameters.setUnitId(devices.getDeviceId());
		parameters.setParity(devices.getParity());
		parameters.setMethod(devices.getMethod());
		parameters.setRegisterMapping(devices.getRegisterMapping());
		parameters.setEncoding(devices.getEncoding());
		parameters.setPort(devices.getPort());
		
		parameters.setRetries(Core.RETRYCOUNT);
		parameters.setType(devices.getType());
		
		parameters.setDeviceMemoryList(devices.getMemoryMappings());

		return parameters;
	}
}
