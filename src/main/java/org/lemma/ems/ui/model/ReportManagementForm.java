package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author RTS Sathish Kumar
 *
 */
public class ReportManagementForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	private long[] uniqueId;

	private List<ReportStatus> config;

	public ReportManagementForm() {
		// NOOP
	}

	public long[] getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long[] uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<ReportStatus> getConfig() {
		return config;
	}

	public void setConfig(List<ReportStatus> config) {
		this.config = config;
	}

	public static class ReportStatus {
		int bitPosition[];
		boolean status[];

		public ReportStatus() {
			// NOOP
		}

		public int[] getBitPosition() {
			return bitPosition;
		}

		public void setBitPosition(int[] bitPosition) {
			this.bitPosition = bitPosition;
		}

		public boolean[] getStatus() {
			return status;
		}

		public void setStatus(boolean[] status) {
			this.status = status;
		}

	}

}
