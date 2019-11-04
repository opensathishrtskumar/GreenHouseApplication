package org.lemma.ems.base.dao.dto;

import java.io.Serializable;

/**
 * @author RTS Sathish  Kumar
 *
 */
public class ReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 306109082835115706L;

	String desc;
	int bitPosition;

	public ReportDTO() {
		// NOOP
	}

	public ReportDTO(String desc, int bitPosition) {
		this.desc = desc;
		this.bitPosition = bitPosition;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

}
