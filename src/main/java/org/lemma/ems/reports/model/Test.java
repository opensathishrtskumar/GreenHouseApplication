package org.lemma.ems.reports.model;

import org.joda.time.LocalDate;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDate endDate = LocalDate.now().minusDays(1);
	    
		LocalDate beginDate = endDate.withDayOfMonth(1);
/*		//int nMonth = ldate.withDayOfMonth(1);
		//System.out.println(ldate.toDate());
*/	}

}
