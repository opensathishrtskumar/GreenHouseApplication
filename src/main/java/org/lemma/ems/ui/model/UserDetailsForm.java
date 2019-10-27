package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
//import org.lemma.ems.base.dao.dto.UserMemoryDTO;

/**
 * @author RTS Sathish Kumar
 *
 */
public class UserDetailsForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	private long uniqueId;
	@NotNull
	private String name;
	@NotNull
	private String emailID;
	@NotNull
	private String password;
	@NotNull
	private int roleID;

	Map<Integer,String> roleList = new LinkedHashMap<>();

	/* COM1-N */
	@NotNull
	@Size(min = 10, message = "Enter Mobile Number", max = 15)
	private String mobileNumber;

	/**
	 * Status in DB {@link DeviceDetailsDAO.Status}
	 */
	private int status;
	/**
	 * During device add, bound with check box
	 */
	private boolean enabled;

	private long createdTimeStamp;
	private long modifiedTimeStamp;

	/**
	 * 
	 */
	private String hashKey;

	public UserDetailsForm() {
		// NOOP

		roleList.put(1,"ADMIN_PLUS");
		roleList.put(2,"ADMIN");
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public long getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(long modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	public Map<Integer,String> getRoleList() {
		return roleList;
	}

	public void setRoleList(Map<Integer,String> roleList) {
		this.roleList = roleList;
	}

}
