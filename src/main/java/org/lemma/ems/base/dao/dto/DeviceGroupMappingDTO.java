	package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceGroupMappingDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long uniqueId;
	private int deviceId;
	private int deviceGroupId;
	private String timeStamp;
	private int coulmnIndex;

	public DeviceGroupMappingDTO() {
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

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
	public int getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(int deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}	
	
}
