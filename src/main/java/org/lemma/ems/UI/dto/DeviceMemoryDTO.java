	package org.lemma.ems.UI.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceMemoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long uniqueId;
	private int deviceId;
	private String memoryMapping;
	private String timeStamp;

	public DeviceMemoryDTO() {
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

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getMemoryMapping() {
		return memoryMapping;
	}

	public void setMemoryMapping(String memoryMapping) {
		this.memoryMapping = memoryMapping;
	}
	
}
