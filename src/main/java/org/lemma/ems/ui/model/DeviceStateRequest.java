package org.lemma.ems.ui.model;

import java.util.List;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DeviceStateRequest extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -408740746346071520L;

	private List<Long> deviceIds;

	private boolean firstRecord;

	public boolean isFirstRecord() {
		return firstRecord;
	}

	public void setFirstRecord(boolean firstRecord) {
		this.firstRecord = firstRecord;
	}

	public List<Long> getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(List<Long> deviceIds) {
		this.deviceIds = deviceIds;
	}

}
