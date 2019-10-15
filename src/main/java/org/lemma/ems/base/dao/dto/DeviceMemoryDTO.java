package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceMemoryDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4855980619075790764L;
	private long uniqueId;
	private long deviceId;
	private String memoryMapping;
	private long createdTimeStamp;

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getMemoryMapping() {
		return memoryMapping;
	}

	public void setMemoryMapping(String memoryMapping) {
		this.memoryMapping = memoryMapping;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

}
