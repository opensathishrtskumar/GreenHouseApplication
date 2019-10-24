package org.lemma.ems.base.core;

import static org.lemma.ems.util.EMSUtility.getPersistRegisters;
import static org.lemma.ems.util.EMSUtility.getRegisterCount;
import static org.lemma.ems.util.EMSUtility.getRegisterReference;

import java.io.Serializable;
import java.util.Map;

import org.lemma.ems.util.EMSUtility;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

public class ExtendedSerialParameter extends SerialParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8541523235063705005L;
	private int unitId;
	// Default retry is 0
	private int retries = 0;
	private int reference;
	private int count;
	private int pointType;
	private int pollDelay;
	private long uniqueId;
	private Map<Long, String> memoryMappings;
	private int[] requiredRegisters;
	private String deviceName;
	private InputRegister[] registeres;
	private boolean status;
	private String registerMapping;
	private String port;
	private String method;

	private Map<String, String> headers;

	public ExtendedSerialParameter(String portName, int baudRate, int flowControlIn, int flowControlOut, int databits,
			int stopbits, int parity) {
		super(portName, baudRate, flowControlIn, flowControlOut, databits, stopbits, parity, false);
	}

	public ExtendedSerialParameter() {
		super();
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
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

	public int getReference() {
		return reference;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPointType() {
		return pointType;
	}

	public void setPointType(int pointType) {
		this.pointType = pointType;
	}

	public int getPollDelay() {
		return pollDelay;
	}

	public void setPollDelay(int pollDelay) {
		this.pollDelay = pollDelay;
	}

	public long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Map<Long, String> getMemoryMappings() {
		return memoryMappings;
	}

	public void setMemoryMappings(Map<Long, String> memoryMappings) {
		this.memoryMappings = memoryMappings;
		// Starting register from where to read
		setReference((int) getRegisterReference(memoryMappings));
		// Total number of registers to be read from Reference register
		setCount(getRegisterCount(memoryMappings));
		// Contains sorted registers to be persisted
		Integer[] registerList = getPersistRegisters(memoryMappings.keySet());
		setRequiredRegisters(EMSUtility.convertWrapper2Int(registerList));
	}

	/**
	 * Creates key for grouped polling - single connection with multiple requests
	 * 
	 * @return
	 */
	public String getGroupKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(getPort());
		builder.append(getBaudRate());
		builder.append(getDatabits());
		builder.append(getParity());
		builder.append(getStopbits());
		return builder.toString();
	}

	public int[] getRequiredRegisters() {
		return requiredRegisters;
	}

	public void setRequiredRegisters(int[] requiredRegisters) {
		this.requiredRegisters = requiredRegisters;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public InputRegister[] getRegisteres() {
		return registeres;
	}

	public void setRegisteres(InputRegister[] registeres) {
		this.registeres = registeres;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public void setPort(String port) {
		// set current port name to super object's property to get connect with
		super.setPortName(port);
		this.port = port;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
