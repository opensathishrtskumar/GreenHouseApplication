package org.lemma.ems.service;

import java.util.List;
import java.util.Locale;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.UserDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.UserDetailsDTO;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.model.DeviceFormDetails;
import org.lemma.ems.ui.model.UserDetailsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author RTS Sathish Kumar
 *
 */
@Repository("userManagementService")
public class UserManagementService {

	@Autowired
	private UserDetailsDAO userDetailsDAO;

	@Autowired
	ReloadableResourceBundleMessageSource msgSource;

	/**
	 * @return
	 */
	public ModelAndView showUserDetailssPage() {
		ModelAndView modelAndView = new ModelAndView("userlist");
		UserDetailsForm userDetailsForm = new UserDetailsForm();
		modelAndView.addObject("userDetailsForm", userDetailsForm);
		modelAndView.addObject("roleList", userDetailsForm.getRoleList());
		return modelAndView;
	}

	/**
	 * @param form
	 * @return
	 */
	public ModelAndView addNewUser(UserDetailsForm form) {
		/**
		 * On successfull insertion
		 * 	1. publish reload event
		 *  2. Redirect back to show devices page with success 
		 */
		//DeviceDetailsForm to DeviceDetailsDTO convertion and insert it
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setName(form.getName());
		//TODO : insert New User using DAO
		
		
		ModelAndView modelAndView = new ModelAndView("redirect:/ems/user/show");
		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("user.added", null, Locale.getDefault()));
		return modelAndView;
	}

}
