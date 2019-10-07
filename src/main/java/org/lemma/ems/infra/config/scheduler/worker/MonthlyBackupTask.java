package org.lemma.ems.infra.config.scheduler.worker;

import java.util.concurrent.Callable;

import org.joda.time.LocalDate;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.constants.QueryConstants;
import org.lemma.ems.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonthlyBackupTask implements Callable<Object>,ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(MonthlyBackupTask.class);
	
	private ApplicationContext applicationContext;
	
	@Override
	public Object call() throws Exception {

		PollingDetailsDAO dao = this.applicationContext.getBean(PollingDetailsDAO.class);

		try {
			// go to last month 1st and backup data older that that
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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
