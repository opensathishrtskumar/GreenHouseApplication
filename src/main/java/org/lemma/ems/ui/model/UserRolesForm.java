package org.lemma.ems.ui.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @author RTS Sathish Kumar
 *
 */
public class UserRolesForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	@NotNull
	private long uniqueId;
	@NotNull
	private String roleType;
	@NotNull
	private int privileges;

	private long createdTimeStamp;
	private long modifiedTimeStamp;

	/**
	 * 
	 */
	private String hashKey;

	public UserRolesForm() {
		// NOOP
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public int getPrivileges() {
		return privileges;
	}

	public void setPrivileges(int privileges) {
		this.privileges = privileges;
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

	@Override
	public String toString() {
		return "UserRolesForm [ uniqueId=" + uniqueId + ",roleType=" + roleType + ", privileges=" + privileges
				+ ", createdTimeStamp=" + createdTimeStamp + ", modifiedTimeStamp=" + modifiedTimeStamp + "]";
	}
}
