package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author RTS Sathish Kumar
 *
 */
public class UserDetailsDTO implements Serializable {

	private long id;
	private String name;
	private String emailId;
	private String password;
	private long roleId;
	private String mobileNumber;
	private int status;
	private long createdTimeStamp;
	private long modifiedTimeStamp;
	private String hashKey;

	/* User Roles details */
	private String roleType;
	private long privileges;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	public long getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(long modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public long getPrivileges() {
		return privileges;
	}

	public void setPrivileges(long privileges) {
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		return "UserDetailsDTO [name=" + name + ", emailId=" + emailId + ", password=" + password + ", roleId=" + roleId
				+ ", mobileNumber=" + mobileNumber + ", status=" + status + ", createdTimeStamp=" + createdTimeStamp
				+ ", modifiedTimeStamp=" + modifiedTimeStamp + "]";
	}

}
