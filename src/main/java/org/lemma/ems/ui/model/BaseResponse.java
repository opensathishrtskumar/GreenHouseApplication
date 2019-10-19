package org.lemma.ems.ui.model;

import java.io.Serializable;

/**
 * @author RTS Sathish Kumar
 *
 */
public class BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5230272671443485706L;
	String uuid;
	int statusCode;
	String statusDescription;

	public BaseResponse() {
		// NOOP
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

}
