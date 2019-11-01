package org.lemma.ems.ui.validator;

import java.util.List;

import org.lemma.ems.ui.model.UserDetailsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

public class UserDetailValidator {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailValidator.class);

	private final UserDetailsForm form;
	private final BindingResult formBinding;

	public UserDetailValidator(UserDetailsForm form, BindingResult formBinding) {
		this.form = form;
		this.formBinding = formBinding;
	}

	public BindingResult validateUserDetailsForm() {

		if (formBinding.hasErrors()) {
			return formBinding;
		}
		
		if(StringUtils.isEmpty(form.getUniqueId() <= 0)) {
			formBinding.rejectValue("uniqueId", "uniqueId.invalid");
		}
		
		if(StringUtils.isEmpty(form.getName())) {
			formBinding.rejectValue("name", "name.invalid");
		}
		
		if(StringUtils.isEmpty(form.getMobileNumber())) {
			formBinding.rejectValue("mobileNumber", "mobileNumber.invalid");
		}
		
		if(StringUtils.isEmpty(form.getRoleID())) {
			formBinding.rejectValue("roleId", "roleId.invalid");
		}
		

		return formBinding;
	}

}
