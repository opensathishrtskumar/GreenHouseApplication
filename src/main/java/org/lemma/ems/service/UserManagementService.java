package org.lemma.ems.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.lemma.ems.base.dao.PageAccessDetailsDAO;
import org.lemma.ems.base.dao.UserDetailsDAO;
import org.lemma.ems.base.dao.UserRolesDAO;
import org.lemma.ems.base.dao.dto.PageAccessDetailsDTO;
import org.lemma.ems.base.dao.dto.UserDetailsDTO;
import org.lemma.ems.base.dao.dto.UserRolesDTO;
import org.lemma.ems.ui.controllers.rest.HelperController;
import org.lemma.ems.ui.model.UserDetailsForm;
import org.lemma.ems.ui.model.UserRolesForm;
import org.lemma.ems.ui.model.UserRolesForm.RoleConfig;
import org.lemma.ems.util.BitUtil;
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
	private UserRolesDAO userRolesDAO;

	@Autowired
	private PageAccessDetailsDAO pageAccessDetailsDAO;

	@Autowired
	ReloadableResourceBundleMessageSource msgSource;

	@Autowired
	private Security security;

	private static final Logger logger = LoggerFactory.getLogger(HelperController.class);

	public static final String VIEW_USER = "SELECT * FROM setup.userdetails ";

	public static final String VIEW_ROLES = "SELECT * FROM setup.userroles";

	public static final String VIEW_PAGES = "SELECT * FROM setup.pageaccessdetails";

	public ModelAndView showUserDetailssPage() {
		ModelAndView modelAndView = new ModelAndView("userlist");
		UserDetailsForm userDetailsForm = new UserDetailsForm();
		modelAndView.addObject("userDetailsForm", userDetailsForm);
		List<UserDetailsDTO> userList = userDetailsDAO.fetchUserDetails(VIEW_USER, new Object[] {});
		modelAndView.addObject("existingUserDetails", userList);
		List<UserRolesDTO> userRoles = userRolesDAO.fetchUserRoles(VIEW_ROLES, new Object[] {});
		modelAndView.addObject("existingUserRoles", userRoles);
		return modelAndView;
	}

	public ModelAndView showUserRolesPage() {
		ModelAndView modelAndView = new ModelAndView("userroles");
		modelAndView.addObject("userRolesForm", new UserRolesForm());
		List<UserRolesDTO> userRoles = userRolesDAO.fetchUserRoles(VIEW_ROLES, new Object[] {});

		Map<Long, String> roleMap = new HashMap<>();
		for (UserRolesDTO role : userRoles) {
			roleMap.put(role.getUniqueId(), role.getRoleType());
		}
		// TODO : required??
		modelAndView.addObject("userRoles", userRoles);

		modelAndView.addObject("existingUserRoles", userRoles);
		List<PageAccessDetailsDTO> pageDetails = pageAccessDetailsDAO.fetchPageAccessDetails(VIEW_PAGES,
				new Object[] {});
		modelAndView.addObject("pageAccessDetails", pageDetails);
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
		UserDetailsDTO dto = mapUserForm2Dto(form, encryptedPassword);

		int result = userDetailsDAO.insertUserDetails(dto);
		logger.info("updating DB. Result=" + result);
		// TODO : insert New User using DAO

		ModelAndView modelAndView = new ModelAndView("redirect:/ems/user/show");

		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("user.added", null, Locale.getDefault()));
		return modelAndView;
	}

	private UserDetailsDTO mapUserForm2Dto(UserDetailsForm form, String encryptedPassword) {
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
		return dto;
	}

	public ModelAndView udpateUser(UserDetailsForm form) {
		/**
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		/*
		 * LocalDate date = LocalDate.now().plusMonths(-1).withDayOfMonth(1); long
		 * timeStamp = Helper.getStartOfDay(date.toDate().getTime());
		 */
		// DeviceDetailsForm to DeviceDetailsDTO convertion and insert it
		UserDetailsDTO dto = mapUpdateUserForm2Dto(form);

		int result = userDetailsDAO.updateUserDetails(dto);
		logger.info("updating DB. Result=" + result);
		// TODO : insert New User using DAO

		ModelAndView modelAndView = new ModelAndView("redirect:/ems/user/show");

		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("user.updated", null, Locale.getDefault()));
		return modelAndView;
	}

	private UserDetailsDTO mapUpdateUserForm2Dto(UserDetailsForm form) {
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setId(form.getUniqueId());
		dto.setName(form.getName());
		dto.setRoleId(form.getRoleID());
		dto.setMobileNumber(form.getMobileNumber());
		dto.setModifiedTimeStamp(System.currentTimeMillis());
		dto.setHashKey("12345");// TODO: calculate hash key value
		return dto;
	}

	public ModelAndView updateUserRoles(UserRolesForm form) {

		ModelAndView modelAndView = new ModelAndView("redirect:/ems/userroles/show");
		modelAndView.addObject("msg", msgSource.getMessage("userroles.update.success", null, Locale.getDefault()));
		long timeStamp = System.currentTimeMillis();

		long[] roleId = form.getRoleId();
		List<RoleConfig> config = form.getConfig();

		logger.trace("Requested to update UserRole details");

		try {
			for (int i = 0; i < roleId.length; i++) {
				UserRolesDTO dto = new UserRolesDTO();
				dto.setUniqueId(roleId[i]);
				RoleConfig status = config.get(i);
				int access = createRolePrivileges(status);
				dto.setPrivileges(access);

				int updatedRecord = userRolesDAO.updateUserRoles(dto);

			}

			logger.debug("UserRoles details updated successfully");

		} catch (Exception e) {
			logger.error("Failed to update user roles {}", e);
			modelAndView.addObject("msg", msgSource.getMessage("userroles.update.failure", null, Locale.getDefault()));
		}

		return modelAndView;
	}

	public int createRolePrivileges(RoleConfig dto) {
		boolean[] status = dto.getStatus();
		int[] bitPosition = dto.getBitPosition();
		long sum = 0;

		// When status is null, set total is 0
		if (status == null)
			return (int) sum;

		// When specific positions status is true, accumulate it
		int index = 0;
		for (int position : bitPosition) {
			if (index < status.length && status[index++])
				sum = BitUtil.setBit(sum, position);
		}

		return (int) sum;
	}

	public ModelAndView addNewRole(UserRolesForm form) {
		/**
		 * On successfull insertion 1. publish reload event 2. Redirect back to show
		 * devices page with success
		 */
		/*
		 * LocalDate date = LocalDate.now().plusMonths(-1).withDayOfMonth(1); long
		 * timeStamp = Helper.getStartOfDay(date.toDate().getTime());
		 */
		// DeviceDetailsForm to DeviceDetailsDTO convertion and insert it
		UserRolesDTO dto = mapUserRolesForm2Dto(form);

		int result = userRolesDAO.insertUserRoles(dto);
		logger.info("updating DB. Result=" + result);
		// TODO : insert New User using DAO

		ModelAndView modelAndView = new ModelAndView("redirect:/ems/userroles/show");

		// Load message from validation.props file
		modelAndView.addObject("msg", msgSource.getMessage("user.added", null, Locale.getDefault()));
		return modelAndView;
	}

	private UserRolesDTO mapUserRolesForm2Dto(UserRolesForm form) {
		UserRolesDTO dto = new UserRolesDTO();
		dto.setRoleType(form.getRoleType());
		dto.setCreatedTimeStamp(System.currentTimeMillis());
		dto.setHashKey("12345");// TODO: calculate hash key value
		return dto;
	}
}
