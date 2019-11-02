package org.lemma.ems.reports.model;

import java.util.List;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DailyReportHolder {

	String companyName;
	String encodedImage;

	String templateName;

	List<DailyReportModel> deviceList;

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

	public List<DailyReportModel> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<DailyReportModel> deviceList) {
		this.deviceList = deviceList;
	}

}
