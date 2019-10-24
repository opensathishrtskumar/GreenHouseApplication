package org.lemma.ems.base.core;

import java.io.Serializable;
import java.util.List;

import com.ghgande.j2mod.modbus.procimg.InputRegister;

/**
 * @author RTS Sathish Kumar
 *
 */
public interface ResponseHandler extends Serializable {

	/**
	 * @return
	 */
	public List<InputRegister[]> getResponseRegisters();

	/**
	 * @param exception
	 */
	public void setException(Exception exception);

	/**
	 * @return
	 */
	public boolean isError();

	/**
	 * 
	 */
	public void pollStart(ExtendedSerialParameter device);
	
	
	/**
	 * @returns actual time of poll
	 */
	public long getTimeOfPoll();
	
	/**
	 * @param parameter
	 */
	public void handleResponse(InputRegister[] responseRegisteres);

	/**
	 * 
	 */
	public void pollCompleted();
}
