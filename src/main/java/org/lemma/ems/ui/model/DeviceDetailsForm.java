package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DeviceDetailsForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	private long uniqueId;
	@NotNull
	private int deviceId;
	@NotNull
	private String deviceName;
	@NotNull
	private int baudRate;
	@NotNull
	private int wordLength;
	@NotNull
	private int stopbit;
	@NotNull
	private String parity;

	/* COM1-N */
	@NotNull
	@Size(min = 4, message = "Select COM Port", max = 6)
	private String port;

	@NotNull
	@Size(min = 1, message = "Select Method(3/4) Port", max = 1)
	private String method;

	@NotNull
	@Size(min = 3, message = "Select Device encoding(rtu/ascii)", max = 5)
	private String encoding;

	// Set default mapping as "MSRF" or "LSRF"
	@NotNull
	@Size(min = 4, message = "Select MSRF/LSRF", max = 4)
	private String registerMapping;

	/**
	 * Status in DB {@link DeviceDetailsDAO.Status}
	 */
	private int status;
	/**
	 * 1 for EMS Devices - READONLY 2 for BMS Devices - READ & WRITE
	 */
	private int type;

	/**
	 * During device add, bound with check box
	 */
	private boolean enabled;
	
	private boolean deleted;
	

	private long createdTimeStamp;
	private long modifiedTimeStamp;

	/**
	 * 
	 */
	private String hashKey;

	private List<ExtendedDeviceMemoryDTO> memoryMappings = new ArrayList<>();

	/* Attributes required for validations */
	private boolean connectionVerified;

	private int accordionIndex;

	public DeviceDetailsForm() {
		// NOOP
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

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public List<ExtendedDeviceMemoryDTO> getMemoryMappings() {
		return memoryMappings;
	}

	public void setMemoryMappings(List<ExtendedDeviceMemoryDTO> memoryMappings) {
		this.memoryMappings = memoryMappings;
	}

	public boolean isConnectionVerified() {
		return connectionVerified;
	}

	public void setConnectionVerified(boolean connectionVerified) {
		this.connectionVerified = connectionVerified;
	}

	public int getAccordionIndex() {
		return accordionIndex;
	}

	public void setAccordionIndex(int accordionIndex) {
		this.accordionIndex = accordionIndex;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "DeviceDetailsForm [deviceId=" + deviceId + ", deviceName=" + deviceName + ", baudRate=" + baudRate
				+ ", wordLength=" + wordLength + ", stopbit=" + stopbit + ", parity=" + parity + ", port=" + port
				+ ", method=" + method + ", encoding=" + encoding + ", registerMapping=" + registerMapping + ", status="
				+ status + ", createdTimeStamp=" + createdTimeStamp + ", modifiedTimeStamp=" + modifiedTimeStamp + "]";
	}
}
