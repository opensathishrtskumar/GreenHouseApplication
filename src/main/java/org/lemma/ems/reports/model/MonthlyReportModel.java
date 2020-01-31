package org.lemma.ems.reports.model;

import java.util.Map;

/**
 * @author RTS Sathish Kumar
 *
 */
public class MonthlyReportModel extends ReportModel {

	private Map<String, Object> consumption;
	private String totalConsumption;
	private String maxConsumptionDate;
	private String minConsumptionDate;
	
	public MonthlyReportModel() {
		// NOOP
	}
	
	public MonthlyReportModel(String feederName, int deviceId, String date) {
		super(feederName, deviceId, date);
	}

	public String getMaxConsumptionDate() {
		return maxConsumptionDate;
	}

	public void setMaxConsumptionDate(String maxConsumptionDate) {
		this.maxConsumptionDate = maxConsumptionDate;
	}

	public String getMinConsumptionDate() {
		return minConsumptionDate;
	}

	public void setMinConsumptionDate(String minConsumptionDate) {
		this.minConsumptionDate = minConsumptionDate;
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
