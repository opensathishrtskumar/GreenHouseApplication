package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.List;

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
	private long roleId[];

	private List<RoleConfig> config;

	private String roleType;

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public List<RoleConfig> getConfig() {
		return config;
	}

	public void setConfig(List<RoleConfig> config) {
		this.config = config;
	}

	public UserRolesForm() {
		// NOOP
	}

	public long[] getRoleId() {
		return roleId;
	}

	public void setRoleId(long[] roleId) {
		this.roleId = roleId;
	}

	public static class RoleConfig {
		@NotNull
		private int bitPosition[];
		@NotNull
		private boolean status[];

		public int[] getBitPosition() {
			return bitPosition;
		}

		public void setBitPosition(int[] bitPosition) {
			this.bitPosition = bitPosition;
		}

		public boolean[] getStatus() {
			return status;
		}

		public void setStatus(boolean[] status) {
			this.status = status;
		}

	}
}
