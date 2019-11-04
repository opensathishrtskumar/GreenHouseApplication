package org.lemma.ems.ui.validator;

import java.util.List;

import org.lemma.ems.ui.model.UserRolesForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

public class UserRoleValidator {

	private static final Logger logger = LoggerFactory.getLogger(UserRoleValidator.class);

	private final UserRolesForm form;
	private final BindingResult formBinding;

	public UserRoleValidator(UserRolesForm form, BindingResult formBinding) {
		this.form = form;
		this.formBinding = formBinding;
	}

	public BindingResult validateUserRolesForm() {

		if (formBinding.hasErrors()) {
			return formBinding;
		}
		
/*		if(StringUtils.isEmpty(form.getUniqueId() <= 0)) {
			formBinding.rejectValue("uniqueId", "uniqueId.invalid");
		}
		
		if(StringUtils.isEmpty(form.getRoleType())) {
			formBinding.rejectValue("roletype", "roletype.invalid");
		}
		
		if(StringUtils.isEmpty(form.getPrivileges())) {
			formBinding.rejectValue("privileges", "privileges.invalid");
		}
*/		
		return formBinding;
	}

}
