package org.lemma.ems.base.core;

/**
 * @author RTS Sathish Kumar
 *
 */
public interface ResponseHandler {
	/**
	 * 
	 */
	public void preStart();

	/**
	 * @param parameter
	 */
	public void handleResponse(ExtendedSerialParameter parameter);

	/**
	 * 
	 */
	public void postStop();
}
