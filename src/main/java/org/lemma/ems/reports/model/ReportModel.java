package org.lemma.ems.reports.model;

/**
 * @author RTS Sathish Kumar
 *
 */
public class ReportModel {

	String feederName;
	int deviceId;
	String date;
	private static final int SHEET_NAME_LEN = 15;

	public ReportModel() {
		// NOOP
	}

	public ReportModel(String feederName, int deviceId, String date) {
		this.feederName = feederName;
		this.deviceId = deviceId;
		this.date = date;
	}

	public String getFeederName() {
		return feederName;
	}

	public String getShortFeederName() {

		if (feederName == null || feederName.isEmpty() || feederName.trim().length() < SHEET_NAME_LEN)
			return feederName;

		return feederName.substring(0, SHEET_NAME_LEN - 1);
	}

	public void setFeederName(String feederName) {
		this.feederName = feederName;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
