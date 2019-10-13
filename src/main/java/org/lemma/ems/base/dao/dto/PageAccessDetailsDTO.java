package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class PageAccessDetailsDTO implements Serializable{

	private long uniqueId;
	private String resourceName;
	private String resourceURL;
	private int bitPosition;
	private long createdTimeStamp;

	public PageAccessDetailsDTO() {
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

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

}
