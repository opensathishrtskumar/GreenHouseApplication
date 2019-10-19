package org.lemma.ems.ui.validator;

import java.util.List;

import org.lemma.ems.base.dao.dto.DeviceMemoryDTO;
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
		
		/* TODO : 
		 * 1. validate device name
		 * 2. deviceid
		 * 3. memory mapping details(name) - 
		 */
		
		
		if(StringUtils.isEmpty(form.getDeviceName())) {
			formBinding.rejectValue("deviceName", "devicename.invalid");
		}
		
		List<DeviceMemoryDTO> memoryMappings = form.getMemoryMappings();
		
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
