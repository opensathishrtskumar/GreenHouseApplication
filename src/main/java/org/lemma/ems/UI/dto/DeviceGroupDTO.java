	package org.lemma.ems.UI.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceGroupDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long uniqueId;
	private String deviceGroupName;
	private String timeStamp;
	private String deviceGroupStatus;
	private int coulmnIndex;

	public DeviceGroupDTO() {
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getCoulmnIndex() {
		return coulmnIndex;
	}

	public void setCoulmnIndex(int coulmnIndex) {
		this.coulmnIndex = coulmnIndex;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getDeviceGroupName() {
		return deviceGroupName;
	}

	public void setDeviceGroupName(String deviceGroupName) {
		this.deviceGroupName = deviceGroupName;
	}

	public String getDeviceGroupStatus() {
		return deviceGroupStatus;
	}

	public void setDeviceGroupStatus(String deviceGroupStatus) {
		this.deviceGroupStatus = deviceGroupStatus;
	}
	
}
