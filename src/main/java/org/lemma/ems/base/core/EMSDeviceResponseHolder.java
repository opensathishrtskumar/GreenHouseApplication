package org.lemma.ems.base.core;

import java.util.ArrayList;
import java.util.List;

import com.ghgande.j2mod.modbus.procimg.InputRegister;

/**
 * @author RTS Sathish Kumar
 *
 */
public class EMSDeviceResponseHolder implements ResponseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8271176954502800382L;
	private List<InputRegister[]> responseRegisters = null;
	private Exception exception;
	private ExtendedSerialParameter device = null;
	private long timeOfPoll = System.currentTimeMillis();

	public EMSDeviceResponseHolder() {
		responseRegisters = new ArrayList<>();
	}

	@Override
	public List<InputRegister[]> getResponseRegisters() {
		return responseRegisters;
	}

	@Override
	public void setException(Exception exception) {
		this.exception = exception;
	}

	@Override
	public void pollStart(ExtendedSerialParameter device) {
		this.device = device;
	}

	@Override
	public void handleResponse(InputRegister[] responseRegisteres) {
		responseRegisters.add(responseRegisteres);
		timeOfPoll = System.currentTimeMillis();
	}

	@Override
	public void pollCompleted() {
		// NOOP
	}

	@Override
	public boolean isError() {
		return exception != null;
	}

	@Override
	public long getTimeOfPoll() {
		return timeOfPoll;
	}

}
