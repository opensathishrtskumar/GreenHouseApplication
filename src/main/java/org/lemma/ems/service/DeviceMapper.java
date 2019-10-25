package org.lemma.ems.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.core.constants.Core;
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

	/**
	 * @param devices
	 * @return
	 */
	public static List<ExtendedSerialParameter> mapDevicesToSerialParams(List<DeviceDetailsDTO> devices) {
		Function<DeviceDetailsDTO, ExtendedSerialParameter> mapper = (device) -> {
			return mapDeviceDetailToExtSerialParam(device);
		};

		return devices.stream().map(mapper).collect(Collectors.toList());
	}

	/**
	 * @param devices
	 * @return
	 */
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
