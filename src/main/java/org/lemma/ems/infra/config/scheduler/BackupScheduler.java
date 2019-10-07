package org.lemma.ems.infra.config.scheduler;

import javax.annotation.PostConstruct;

import org.lemma.ems.infra.config.scheduler.worker.ArchiveCleanupTask;
import org.lemma.ems.infra.config.scheduler.worker.DailyBackUpTask;
import org.lemma.ems.infra.config.scheduler.worker.MonthlyBackupTask;
import org.lemma.ems.util.concurrency.ConcurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@EnableScheduling
public class BackupScheduler {

	private static final Logger logger = LoggerFactory.getLogger(BackupScheduler.class);

	@Autowired
	private ConcurrencyUtils concurrencyUtil;

	@PostConstruct
	public void init() {
		logger.trace("BackUp Scheduler Initialized");
	}

	
	/**
	 * Daily table to monthly table backup
	 */
	@Scheduled(cron = "${cron.dailybackup}")
	public void backUpDaily() {
		logger.debug("daily backup initializing...");
		concurrencyUtil.execute(new DailyBackUpTask());
		logger.debug("daily backup initialized...");
	}

	
	/**
	 * Monthly table to archive table backup
	 */
	@Scheduled(cron = "${cron.monthlybackup}")
	public void backUpMonthly() {
		logger.debug("monthly backup initializing...");
		concurrencyUtil.execute(new MonthlyBackupTask());
		logger.debug("monthly backup initialized...");
	}

	
	/**
	 * clean up record older than 6 months
	 */
	@Scheduled(cron = "${cron.archieve}")
	public void archieveBackup() {
		logger.debug("archive backup initializing...");
		concurrencyUtil.execute(new ArchiveCleanupTask());
		logger.debug("archive backup initialized...");
	}

}
