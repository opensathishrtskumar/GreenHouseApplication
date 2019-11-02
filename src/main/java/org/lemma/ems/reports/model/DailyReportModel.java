package org.lemma.ems.reports.model;

import java.util.Map;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DailyReportModel extends ReportModel {

	private Map<String, Object> consumption;
	private String totalConsumption;

	public DailyReportModel() {
		// NOOP
	}

	public DailyReportModel(String feederName, int deviceId, String date) {
		super(feederName, deviceId, date);
	}

	public Map<String, Object> getConsumption() {
		return consumption;
	}

	public void setConsumption(Map<String, Object> consumption) {
		this.consumption = consumption;
	}

	public Object getValue(String key) {
		return this.consumption.get(key);
	}

	public String getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(String totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

}
