package org.lemma.ems.base.core;

import java.io.Serializable;
import java.util.List;

import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;

import com.ghgande.j2mod.modbus.util.SerialParameters;

public class ExtendedSerialParameter extends SerialParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8541523235063705005L;
	private int unitId;
	// Default retry is 0
	private int retries = 0;
	private long uniqueId;
	private String deviceName;
	private String registerMapping;
	private String port;
	private String method;
	
	/**
	 * EMS/BMS
	 */
	private int type;

	private List<ExtendedDeviceMemoryDTO> deviceMemoryList;
	private Class<? extends ResponseHandler> responseHandler;

	public ExtendedSerialParameter(String portName, int baudRate, int flowControlIn, int flowControlOut, int databits,
			int stopbits, int parity) {
		super(portName, baudRate, flowControlIn, flowControlOut, databits, stopbits, parity, false);
		responseHandler = EMSDeviceResponseHolder.class;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @returns key consists of some configuration parameter
	 *          {@literal port,baudrate,wordlength,parity and stopbit}
	 */
	public String getGroupByKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(getPort());
		builder.append(getBaudRate());
		builder.append(getDatabits());
		builder.append(getParity());
		builder.append(getStopbits());
		return builder.toString();
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getRegisterMapping() {
		return registerMapping;
	}

	public void setRegisterMapping(String registerMapping) {
		this.registerMapping = registerMapping;
	}

	public String getPort() {
		return port;
	}

	/**
	 * <tt>set current port name to super object's property to get connect with</tt>
	 * 
	 * @param port
	 */
	public void setPort(String port) {
		super.setPortName(port);
		this.port = port;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<ExtendedDeviceMemoryDTO> getDeviceMemoryList() {
		return deviceMemoryList;
	}

	public void setDeviceMemoryList(List<ExtendedDeviceMemoryDTO> deviceMemoryList) {
		this.deviceMemoryList = deviceMemoryList;
	}

	public Class<? extends ResponseHandler> getResponseHandler() {
		return responseHandler;
	}

}
