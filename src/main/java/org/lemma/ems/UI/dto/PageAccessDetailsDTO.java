package org.lemma.ems.UI.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class PageAccessDetailsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long uniqueId;
	private String resourceName;
	private String resourceURL;
	private int bitPosition;
	private String timeStamp;

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
	
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
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
