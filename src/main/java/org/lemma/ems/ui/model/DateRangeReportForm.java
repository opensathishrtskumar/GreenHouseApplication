
package org.lemma.ems.ui.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
public class DateRangeReportForm implements Serializable {

	private static final long serialVersionUID = 8737934591484167924L;

	@NotNull
	@NotEmpty
	private String deviceName;

	@NotNull
	@NotEmpty
	private String memoryMappingDetails;

	@NotNull
	@NotEmpty
	private String reportStartTime;

	@NotNull
	@NotEmpty
	private String reportEndTime;

	@NotNull
	@Size(min = 1, message = "Select Report Type", max = 3)
	private String reportType;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMemoryMappingDetails() {
		return memoryMappingDetails;
	}

	public void setMemoryMappingDetails(String memoryMappingDetails) {
		this.memoryMappingDetails = memoryMappingDetails;
	}

	public String getReportStartTime() {
		return reportStartTime;
	}

	public void setReportStartTime(String reportStartTime) {
		this.reportStartTime = reportStartTime;
	}

	public String getReportEndTime() {
		return reportEndTime;
	}

	public void setReportEndTime(String reportEndTime) {
		this.reportEndTime = reportEndTime;
	}
}