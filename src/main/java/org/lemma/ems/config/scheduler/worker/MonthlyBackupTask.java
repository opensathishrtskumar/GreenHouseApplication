package org.lemma.ems.config.scheduler.worker;

import java.util.concurrent.Callable;

import org.joda.time.LocalDate;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.config.listener.AppContextAware;
import org.lemma.ems.constants.QueryConstants;
import org.lemma.ems.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonthlyBackupTask implements Callable<Object>{

	private static final Logger logger = LoggerFactory.getLogger(MonthlyBackupTask.class);
	
	@Override
	public Object call() throws Exception {

		PollingDetailsDAO dao = AppContextAware.getContext().getBean(PollingDetailsDAO.class);

		try {
			//go to last month 1st and backup data older that that
			LocalDate date = LocalDate.now().plusMonths(-1).withDayOfMonth(1);
			long timeStamp = Helper.getStartOfDay(date.toDate().getTime());

			int rowsBackedUp = dao.executeQuery(QueryConstants.MONTHLY_2_ARVHIVE_BACKUP_QUERY,
					new Object[] { timeStamp });
			logger.info("Rows backed up is {}", rowsBackedUp);

			int rowsDeleted = dao.executeQuery(QueryConstants.MONTHLY_2_ARVHIVE_BACKUP_DELETE_QUERY,
					new Object[] { timeStamp });
			logger.info("Rows deleted after backup is {}", rowsDeleted);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		
		return "Monthly backup completed";
	}
	
	/*public static void main(String[] args) {
		LocalDate date = LocalDate.now().plusMonths(-1).withDayOfMonth(1);
		System.out.println(date.toDate().getTime());
	}*/
}
