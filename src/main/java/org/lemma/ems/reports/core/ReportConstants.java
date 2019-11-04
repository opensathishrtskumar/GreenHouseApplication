package org.lemma.ems.reports.core;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class ReportConstants {

	/**
	 * @author RTS Sathish  Kumar
	 *
	 */
	public enum Reports {

		DAILY_CUMULATIVE_RPT(1, "daily_cumulative_rpt.xls","Daily Cumulative Report"),
		MONTHLY_CUMULATIVE_RPT(2, "monthly_cumulative_rpt.xls","Monthly Cumulative Report"),
		SUMMARY_RPT(3, "summary_rpt.xls", "Summary Report"),
		ELOBORATE_RPT(4, "eloborate_rpt.xls" , "Detailed Data");

		int type;
		String templateName;
		String desc;

		private Reports(int type, String templateName, String desc) {
			this.type = type;
			this.templateName = templateName;
			this.desc = desc;
		}

		public int getType() {
			return type;
		}

		public String getTemplateName() {
			return templateName;
		}

		public String getDesc() {
			return desc;
		}
	}
}
