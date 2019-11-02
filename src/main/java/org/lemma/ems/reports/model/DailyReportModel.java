package org.lemma.ems.reports.model;

import java.util.Date;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DailyReportModel extends ReportModel {

	public DailyReportModel() {

	}

	public DailyReportModel(String feederName, int deviceId, String date) {
		super(feederName, deviceId, date);
	}

}
