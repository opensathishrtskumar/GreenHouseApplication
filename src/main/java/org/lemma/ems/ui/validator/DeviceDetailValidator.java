package org.lemma.ems.ui.validator;

import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

public class DeviceDetailValidator {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDetailValidator.class);

	private final DeviceDetailsForm form;
	private final BindingResult formBinding;

	public DeviceDetailValidator(DeviceDetailsForm form, BindingResult formBinding) {
		this.form = form;
		this.formBinding = formBinding;
	}

	public BindingResult validateDeviceDetailsForm() {

		if (formBinding.hasErrors()) {
			return formBinding;
		}
		
		if(StringUtils.isEmpty(form.getDeviceId() <= 0)) {
			formBinding.rejectValue("deviceName", "devicename.invalid");
		}
		
		if(StringUtils.isEmpty(form.getDeviceName())) {
			formBinding.rejectValue("deviceId", "deviceid.invalid");
		}
		
		if(StringUtils.isEmpty(form.getDeviceName())) {
			formBinding.rejectValue("port", "comport.invalid");
		}
		
		List<ExtendedDeviceMemoryDTO> memoryMappings = form.getMemoryMappings();
		
		//assumption that Test Connection API validates completly, so basic validation is alone implemented 
		if(memoryMappings == null || memoryMappings.isEmpty()) {
			formBinding.rejectValue("memoryMappings[0].memoryMapping", "memorymapping.invalid");
		} else {
			int index = 0;
			
			for(DeviceMemoryDTO memory : memoryMappings) {
				String memoryMapping = memory.getMemoryMapping();
				/**
				 * Validate individual mappings - Core.MemoryMapping 
				 */
				if(StringUtils.isEmpty(memoryMapping)) {
					formBinding.rejectValue(String.format("memoryMappings[%s].memoryMapping",index), "memorymapping.invalid");
				}
			}
		}

		return formBinding;
	}

}
