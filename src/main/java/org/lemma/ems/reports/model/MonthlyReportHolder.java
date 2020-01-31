package org.lemma.ems.reports.model;

import java.util.List;

/**
 * @author RTS Sathish Kumar
 *
 */
public class MonthlyReportHolder {

	String companyName;
	String encodedImage;
	String templateName;
	String month;

	List<MonthlyReportModel> deviceList;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}

	public List<MonthlyReportModel> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<MonthlyReportModel> deviceList) {
		this.deviceList = deviceList;
	}

}