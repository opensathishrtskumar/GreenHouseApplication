package org.lemma.ems.ui.model;

import java.util.ArrayList;
import java.util.List;

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

	private List<DeviceState> deviceStates = new ArrayList<>();

	public List<DeviceState> getDeviceStates() {
		return deviceStates;
	}

	public void setDeviceStates(List<DeviceState> deviceStates) {
		this.deviceStates = deviceStates;
	}

	public static class DeviceState {
		private long deviceId;

		private List<PollingDetailsDTO> deviceStates;

		public DeviceState() {

		}

		public DeviceState(long deviceId, List<PollingDetailsDTO> deviceStates) {
			this.deviceId = deviceId;
			this.deviceStates = deviceStates;
		}

		public long getDeviceId() {
			return deviceId;
		}

		public void setDeviceId(long deviceId) {
			this.deviceId = deviceId;
		}

		public List<PollingDetailsDTO> getDeviceStates() {
			return deviceStates;
		}

		public void setDeviceStates(List<PollingDetailsDTO> deviceStates) {
			this.deviceStates = deviceStates;
		}

	}

}
