package org.lemma.ems.ui.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lemma.ems.service.UserManagementService;
import org.lemma.ems.ui.model.SignupForm;
import org.lemma.ems.ui.model.UserDetailsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
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

	@RequestMapping(value = "/ems/user/show", method = RequestMethod.GET)
	public ModelAndView showUsers() {
		return userManagementService.showUserDetailssPage();
	}

	@RequestMapping(value = "/ems/user/add", method = RequestMethod.POST)
	public ModelAndView checkDeviceConnection(@ModelAttribute("userDetailsForm") UserDetailsForm request) {
		logger.debug("User details add request {}", request);
		return userManagementService.addNewUser(request);
	}
	
}
