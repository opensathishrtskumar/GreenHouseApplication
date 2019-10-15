package org.lemma.ems.base.dao.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sathish Kumar
 *
 */
public class DeviceDetailsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7143828630622819390L;
	private long uniqueId;
	private int deviceId;
	private String deviceName;
	private int baudRate;
	private int wordLength;
	private int stopbit;
	private String parity;
	private String port;
	private String method;
	// Set default mapping as "MSRF" or "LSRF"
	private String registerMapping;
	private int status;
	/**
	 * 1 for EMS Devices - READONLY 2 for BMS Devices - READ & WRITE
	 */
	private int type;

	private long createdTimeStamp;
	private long modifiedTimeStamp;
	private String hashKey;

	private List<DeviceMemoryDTO> memoryMappings;

	public DeviceDetailsDTO() {
		//
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

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}

	public int getStopbit() {
		return stopbit;
	}

	public void setStopbit(int stopbit) {
		this.stopbit = stopbit;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRegisterMapping() {
		return registerMapping;
	}

	public void setRegisterMapping(String registerMapping) {
		this.registerMapping = registerMapping;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public List<DeviceMemoryDTO> getMemoryMappings() {
		return memoryMappings;
	}

	public void setMemoryMappings(List<DeviceMemoryDTO> memoryMappings) {
		this.memoryMappings = memoryMappings;
	}
}
