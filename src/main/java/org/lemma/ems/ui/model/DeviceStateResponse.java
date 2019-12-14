package org.lemma.ems.ui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemma.ems.base.dao.dto.PollingDetailsDTO;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DeviceStateResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2486447764133388764L;

	private Map<Long, List<PollingDetailsDTO>> deviceStates = new HashMap<>();

	public Map<Long, List<PollingDetailsDTO>> getDeviceStates() {
		return deviceStates;
	}

	public void setDeviceStates(Map<Long, List<PollingDetailsDTO>> deviceStates) {
		this.deviceStates = deviceStates;
	}
}
