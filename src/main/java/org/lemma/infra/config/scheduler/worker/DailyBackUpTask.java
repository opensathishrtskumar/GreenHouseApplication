package org.lemma.infra.config.scheduler.worker;

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
public class DailyBackUpTask implements Callable<Object>,ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(MonthlyBackupTask.class);
	
	private ApplicationContext applicationContext;
	
	@Override
	public Object call() throws Exception {
		logger.info("Processing daily back task...");

		PollingDetailsDAO dao = this.applicationContext.getBean(PollingDetailsDAO.class);

		try {
			LocalDate date = LocalDate.now().plusDays(-2);
			long timeStamp = Helper.getEndOfDay(date.toDate().getTime());

			int rowsBackedUp = dao.executeQuery(QueryConstants.DAILY_2_MONTHLY_BACKUP_QUERY,
					new Object[] { timeStamp });
			logger.info("Rows backed up is {}", rowsBackedUp);

			int rowsDeleted = dao.executeQuery(QueryConstants.DAILY_2_MONTHLY_BACKUP__DELETE_QUERY,
					new Object[] { timeStamp });
			logger.info("Rows deleted after backup is {}", rowsDeleted);
		} catch (Exception e) {
			logger.error("Error", e);
		}

		logger.info("Processed daily back task...");
		return "Daily back up completed";
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
