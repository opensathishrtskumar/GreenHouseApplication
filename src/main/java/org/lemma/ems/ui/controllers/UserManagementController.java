package org.lemma.ems.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lemma.ems.service.UserManagementService;
import org.lemma.ems.ui.model.SignupForm;
import org.lemma.ems.ui.model.UserDetailsForm;
import org.lemma.ems.ui.model.UserRolesForm;
import org.lemma.ems.ui.validator.UserDetailValidator;
import org.lemma.ems.ui.validator.UserRoleValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ems.security.Security;

@Controller
public class UserManagementController {

	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);

	@Autowired
	private Environment environment;

	@Autowired
	private Security security;

	@Autowired
	UserManagementService userManagementService;

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public void showSignIn() {
		//
	}

	@RequestMapping(value = "/signout", method = RequestMethod.GET)
	public void showSignout() {
		//
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView showRegistration(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("signup");
		view.addObject("signupForm", new SignupForm());
		return view;
	}

	@RequestMapping(value = "/ems/homeSignedIn", method = RequestMethod.GET)
	public ModelAndView showHomeSignedIn(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("homeSignedIn");
	}

	@RequestMapping(value = "/ems/user", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("user");
	}

	@RequestMapping(value = "/ems/user/show", method = RequestMethod.GET)
	public ModelAndView showUsers() {
		return userManagementService.showUserDetailssPage();
	}

	@RequestMapping(value = "/ems/user/add", method = RequestMethod.POST)
	public ModelAndView checkDeviceConnection(@ModelAttribute("userDetailsForm") UserDetailsForm form,
			BindingResult formBinding, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.debug("User details add request {}", form);
		ModelAndView modelAndView = userManagementService.showUserDetailssPage();

		UserDetailValidator validator = new UserDetailValidator(form, formBinding);
		validator.validateUserDetailsForm();

		// Return errors if there is any, along with memoryMappings to iterate and Form
		// Binding
		if (formBinding.hasErrors()) {
			modelAndView.addObject("userDetailsForm", form);
			return modelAndView;
		}

		return userManagementService.addNewUser(form);
	}

	@RequestMapping(value = "/ems/user/update", method = RequestMethod.POST)
	public ModelAndView updateUserDetails(@ModelAttribute("userDetailsForm") UserDetailsForm form,
			BindingResult formBinding, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.debug("User details update request {}", form);
		ModelAndView modelAndView = userManagementService.showUserDetailssPage();

		UserDetailValidator validator = new UserDetailValidator(form, formBinding);
		validator.validateUserDetailsForm();

		// Return errors if there is any, along with memoryMappings to iterate and Form
		// Binding
		if (formBinding.hasErrors()) {
			modelAndView.addObject("userDetailsForm", form);
			return modelAndView;
		}

		return userManagementService.udpateUser(form);
	}

	@RequestMapping(value = "/ems/userroles/show", method = RequestMethod.GET)
	public ModelAndView showRoles() {
		return userManagementService.showUserRolesPage();
	}

	@RequestMapping(value = "/ems/userroles/manage", method = RequestMethod.POST)
	public ModelAndView updateUserRoles(@ModelAttribute("userRolesForm") UserRolesForm form, BindingResult formBinding,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		logger.debug("User Roles update request {}", form);
		ModelAndView modelAndView = userManagementService.showUserRolesPage();

		UserRoleValidator validator = new UserRoleValidator(form, formBinding);
		validator.validateUserRolesForm();

		// Return errors if there is any, along with memoryMappings to iterate and Form
		// Binding
		if (formBinding.hasErrors()) {
			modelAndView.addObject("userRolesForm", form);
			return modelAndView;
		}

		return userManagementService.udpateRole(form);
	}

}
