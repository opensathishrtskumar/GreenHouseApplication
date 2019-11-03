package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class UserRolesDTO implements Serializable {

	private long uniqueId;
	private String roleType;
	private int privileges;
	private long createdTimeStamp;
	private long modifiedTimeStamp;
	private String hashKey;

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

	public boolean isBitPositionSet(String privileges, String bitPosition) {
		int totalValue = Integer.parseInt(privileges);
		int checkBit = Integer.parseInt(bitPosition);
		int returnValue =(totalValue >> checkBit) & 1; 
		return (returnValue==1)?true: false;
		/*
		 * UserRolesDTO details = new UserRolesDTO(); int privileges =
		 * details.getPrivileges();
		 */
		 
	}
}
