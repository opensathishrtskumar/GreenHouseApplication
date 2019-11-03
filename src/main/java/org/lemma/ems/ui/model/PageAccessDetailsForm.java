package org.lemma.ems.ui.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @author RTS Sathish Kumar
 *
 */
public class PageAccessDetailsForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	@NotNull
	private long uniqueId;
	@NotNull
	private String resourceName;
	@NotNull
	private String resourceURL;
	@NotNull
	private int bitPosition;

	private long createdTimeStamp;

	public PageAccessDetailsForm() {
		// NOOP
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceURL() {
		return resourceURL;
	}

	public void setResourceURL(String resourceURL) {
		this.resourceURL = resourceURL;
	}

	public int getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	@Override
	public String toString() {
		return "UserRolesForm [ uniqueId=" + uniqueId + ",resourceName=" + resourceName + ", resourceURL=" + resourceURL
				+ ", bitPosition=" + bitPosition + ", createdTimeStamp=" + createdTimeStamp + "]";
	}
}
