package org.lemma.ems.service;

import java.util.List;
import java.util.Locale;

import org.joda.time.LocalDate;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.UserDetailsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.UserDetailsDTO;
import org.lemma.ems.ui.controllers.rest.HelperController;
import org.lemma.ems.ui.model.DeviceDetailsForm;
import org.lemma.ems.ui.model.DeviceFormDetails;
import org.lemma.ems.ui.model.UserDetailsForm;
import org.lemma.ems.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

import com.ems.security.Security;

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
	@Autowired
	private Security security;

	private static final Logger logger = LoggerFactory.getLogger(HelperController.class);

	public static final String INSERT_USER = "insert into setup.userdetails "
			+ "(name, emailid, password, roleid, mobilenumber,status,createdtimestamp,modifiedtimestamp,hashkey)"
			+ " values(?,?,?,?,?,?,?,?,?)";

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
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		/*
		 * LocalDate date = LocalDate.now().plusMonths(-1).withDayOfMonth(1); long
		 * timeStamp = Helper.getStartOfDay(date.toDate().getTime());
		 */
		// DeviceDetailsForm to DeviceDetailsDTO convertion and insert it
		String encryptedPassword = "";
		try {
			encryptedPassword = this.security.encrypt(form.getPassword());
		} catch (Exception e) {
			logger.error("Password encryption error {}", e);
		}
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setName(form.getName());
		dto.setEmailId(form.getEmailID());
		dto.setPassword(encryptedPassword);
		dto.setRoleId(form.getRoleID());
		dto.setMobileNumber(form.getMobileNumber());
		dto.setStatus(form.getStatus());
		dto.setCreatedTimeStamp(System.currentTimeMillis());
		dto.setModifiedTimeStamp(System.currentTimeMillis());
		dto.setHashKey("12345");// TODO: calculate hash key value
		int result = userDetailsDAO.executeQuery(INSERT_USER,
				new Object[] { dto.getName(), dto.getEmailId(), dto.getPassword(), dto.getRoleId(),
						dto.getMobileNumber(), dto.getStatus(), dto.getCreatedTimeStamp(), dto.getModifiedTimeStamp(),
						dto.getHashKey() });
		logger.info("updating DB. Result=" + result);
		// TODO : insert New User using DAO

		ModelAndView modelAndView = new ModelAndView("redirect:/ems/user/show");
		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("user.added", null, Locale.getDefault()));
		return modelAndView;
	}

}
