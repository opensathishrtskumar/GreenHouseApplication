package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author RTS Sathish  Kumar
 *
 */
public class DeviceReportMasterDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long deviceid;
	private int type;
	private int status;
	private long createdtimestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(long deviceid) {
		this.deviceid = deviceid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreatedtimestamp() {
		return createdtimestamp;
	}

	public void setCreatedtimestamp(long createdtimestamp) {
		this.createdtimestamp = createdtimestamp;
	}

}
