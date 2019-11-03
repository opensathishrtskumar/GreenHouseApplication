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

		DAILY_CUMULATIVE_RPT(1, "daily_cumulative_rpt.xls"),
		MONTHLY_CUMULATIVE_RPT(2, "monthly_cumulative_rpt.xls"),
		SUMMARY_RPT(3, "summary_rpt.xls"),
		ELOBORATE_RPT(4, "eloborate_rpt.xls");

		int type;
		String templateName;

		private Reports(int type, String templateName) {
			this.type = type;
			this.templateName = templateName;
		}

		public int getType() {
			return type;
		}

		public String getTemplateName() {
			return templateName;
		}
	}
}
