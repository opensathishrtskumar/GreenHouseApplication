package org.lemma.ems.UI.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceGroupDTO implements Serializable{

	private long uniqueId;
	private String deviceGroupName;
	private long createdTimeStamp;
	private String deviceGroupStatus;
	private int coulmnIndex;

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
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
