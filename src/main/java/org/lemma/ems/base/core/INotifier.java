package org.lemma.ems.base.core;

import com.ghgande.j2mod.modbus.procimg.InputRegister;

/**
 * @author RTS Sathish Kumar
 *
 */
public interface INotifier {

	InputRegister[] responseRegisteres = null;
	Exception exception = null;

	public default InputRegister[] getResponseRegisters() {
		return responseRegisteres;
	}

	public void setResponseRegisters(InputRegister[] responseRegisteres, long deviceUniqueId);

	public default boolean isFailure() {
		return exception == null;
	}

	public default Exception getException() {
		return exception;
	}

	public void setException(Exception exception);
}
