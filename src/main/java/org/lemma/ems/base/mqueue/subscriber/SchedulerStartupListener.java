package org.lemma.ems.base.mqueue.subscriber;

import static org.lemma.ems.scheduler.util.SchedulerUtil.createJob;
import static org.lemma.ems.scheduler.util.SchedulerUtil.createTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemma.ems.base.dao.SchedulesDAO;
import org.lemma.ems.base.dao.dto.SchedulesDTO;
import org.lemma.ems.base.mqueue.ReceiverConfig;
import org.lemma.ems.scheduler.base.AutowiringSpringBeanJobFactory;
import org.lemma.ems.scheduler.jobs.SimpleStoppableJob;
import org.lemma.ems.scheduler.util.JobUtil;
import org.quartz.InterruptableJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@EnableJms
public class SchedulerStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerStartupListener.class);

	/**
	 * Locks to prevent multiple Same Onetime job
	 */
	private static final Object SCHEDULER_MUTEX = new Object();

	@Value("${scheduler.enabled}")
	private boolean schedulerEnabled;

	@Autowired
	private SchedulesDAO schedulesDAO;

	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private ApplicationContext context;

	/* constants */
	private static final String TRIGGER_SCHEDULES_TXT = "TRIGGER.SCHEDULES.TOPIC";

	public enum Topics {
		TRIGGER_SCHEDULES(TRIGGER_SCHEDULES_TXT);
		String topic;

		private Topics(String topic) {
			this.topic = topic;
		}

		public String getTopic() {
			return topic;
		}
	}

	/**
	 * When {@link scheduler.enabled} true 1. loads all schedules 2. Active
	 * schedules are scheduled / rescheduled 3. Inactive schedule are unscheduled /
	 * stopped
	 * 
	 * @param message
	 */
	@JmsListener(destination = TRIGGER_SCHEDULES_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void triggerSchedules(Object message) {
		logger.info("triggerSchedules Scheduler status {} msg {}", schedulerEnabled, message);

		Scheduler scheduler = schedulerFactory.getScheduler();

		/* Trigger schedles only when enabled */
		if (schedulerEnabled) {

			List<SchedulesDTO> activeSchedules = schedulesDAO.fetchAllSchedules();

			// Not allowed to trigger multiple times
			synchronized (SCHEDULER_MUTEX) {
				scheduleJobs(scheduler, activeSchedules);
			}
		}
	}

	private void scheduleJobs(Scheduler scheduler, List<SchedulesDTO> activeSchedules) {
		for (SchedulesDTO schedule : activeSchedules) {
			try {
				Class<QuartzJobBean> forName = createClass(schedule);

				TriggerKey triggerKey = TriggerKey.triggerKey(schedule.getJobKey(), schedule.getGroupKey());
				JobDetail jobDetail = createJob(schedule, forName, context);
				JobKey jobKey = JobKey.jobKey(schedule.getJobKey(), schedule.getGroupKey());

				/*
				 * Get job existance status - unschedule all task and reschedule if status is
				 * active
				 */
				dropExistingJob(scheduler, schedule, triggerKey, jobKey);

				if(schedule.getType() == SchedulesDAO.Status.INACTIVE.getStatus())
					continue;
				
				if (schedule.getStatus() == SchedulesDAO.Type.CRONJOB.getType()) {
					Trigger cronTriggerBean = createTrigger(schedule, jobDetail);
					scheduler.scheduleJob(jobDetail, cronTriggerBean);
				} else if (schedule.getStatus() == SchedulesDAO.Type.ONETIMEJOB.getType()) {
					scheduleOneTimeJob(schedule, new Date());
				}

				logger.debug("{} job rescheduled", schedule.getClassName());

			} catch (Exception e) {
				logger.error("Error scheduling tasks {} {}", schedule.getClassName(), e);
			}
		}
	}

	private void dropExistingJob(Scheduler scheduler, SchedulesDTO schedule, TriggerKey triggerKey, JobKey jobKey)
			throws SchedulerException {
		boolean triggerExists = scheduler.checkExists(triggerKey);
		boolean jobExists = scheduler.checkExists(jobKey);
		List<Object> simpleStoppableJobs = AutowiringSpringBeanJobFactory.getSimpleStoppableJob(jobKey);

		logger.debug("{} job existane {}", schedule.getClassName(), (triggerExists | jobExists));

		if (triggerExists || jobExists) {
			scheduler.unscheduleJob(triggerKey);
			scheduler.interrupt(jobKey);
			scheduler.deleteJob(jobKey);
			logger.debug("{} job unscheduled", schedule.getClassName());
		}

		if (simpleStoppableJobs != null) {
			for (Object simpleStoppableJob : simpleStoppableJobs) {
				try {
					((SimpleStoppableJob) simpleStoppableJob).interruptStoppable();
					((InterruptableJob) simpleStoppableJob).interrupt();
				} catch (Exception e) {
					logger.error("Failed interrupting onetime job {}", e);
				} finally {
					AutowiringSpringBeanJobFactory.removeSimpleStoppableJob(jobKey);
				}
			}
		}
	}

	/**
	 * Schedule a job by jobName at given date.
	 */
	public boolean scheduleOneTimeJob(SchedulesDTO schedule, Date date) {
		logger.debug("Request received to scheduleJob one time");

		try {
			Class<QuartzJobBean> forName = createClass(schedule);
			TriggerKey triggerKey = TriggerKey.triggerKey(schedule.getJobKey(), schedule.getGroupKey());
			JobDetail jobDetail = createJob(schedule, forName, context);

			logger.debug("creating trigger for key : {}", triggerKey);
			Trigger cronTriggerBean = JobUtil.createSingleTrigger(triggerKey.toString(), date,
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW, jobDetail);

			Scheduler scheduler = schedulerFactory.getScheduler();

			scheduler.scheduleJob(jobDetail, cronTriggerBean);
			logger.debug("Job with key {} scheduled successfully {}", triggerKey);
			return true;
		} catch (Exception e) {
			logger.debug("Scheduler Exception Job with key {} failed {}", schedule.getClassName(), e);
		}

		return false;
	}

	/**
	 * @param schedule
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class<QuartzJobBean> createClass(SchedulesDTO schedule) throws ClassNotFoundException {
		return (Class<QuartzJobBean>) Class.forName(schedule.getClassName());
	}

	/**
	 * Get all jobs
	 */
	public List<Map<String, Object>> getAllJobs() {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();

			for (String groupName : scheduler.getJobGroupNames()) {

				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();

					// get job's trigger
					List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
					Date scheduleTime = triggers.get(0).getStartTime();
					Date nextFireTime = triggers.get(0).getNextFireTime();
					Date lastFiredTime = triggers.get(0).getPreviousFireTime();

					Map<String, Object> map = new HashMap<>();
					map.put("jobName", jobName);
					map.put("groupName", jobGroup);
					map.put("scheduleTime", scheduleTime);
					map.put("lastFiredTime", lastFiredTime);
					map.put("nextFireTime", nextFireTime);

					if (isJobRunning(jobName, jobGroup)) {
						map.put("jobStatus", "RUNNING");
					} else {
						String jobState = getJobState(jobName, jobGroup);
						map.put("jobStatus", jobState);
					}

					list.add(map);
				}

			}
		} catch (SchedulerException e) {
			logger.error("SchedulerException while fetching all jobs. error message : {}", e.getMessage());
		}
		return list;
	}

	/**
	 * Check if job is already running
	 */
	private boolean isJobRunning(String jobName, String groupName) {

		String jobKey = jobName;
		String groupKey = groupName;

		try {

			List<JobExecutionContext> currentJobs = schedulerFactory.getScheduler().getCurrentlyExecutingJobs();
			if (currentJobs != null) {
				for (JobExecutionContext jobCtx : currentJobs) {
					String jobNameDB = jobCtx.getJobDetail().getKey().getName();
					String groupNameDB = jobCtx.getJobDetail().getKey().getGroup();
					if (jobKey.equalsIgnoreCase(jobNameDB) && groupKey.equalsIgnoreCase(groupNameDB)) {
						return true;
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error("SchedulerException while checking job with key :" + jobKey + " is running. error message :"
					+ e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * Get the current state of job
	 */
	private String getJobState(String jobName, String groupName) {
		try {
			JobKey jobKey = JobKey.jobKey(jobName, groupName);

			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);

			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

			if (triggers != null && !triggers.isEmpty()) {
				for (Trigger trigger : triggers) {
					TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

					if (TriggerState.PAUSED.equals(triggerState)) {
						return "PAUSED";
					} else if (TriggerState.BLOCKED.equals(triggerState)) {
						return "BLOCKED";
					} else if (TriggerState.COMPLETE.equals(triggerState)) {
						return "COMPLETE";
					} else if (TriggerState.ERROR.equals(triggerState)) {
						return "ERROR";
					} else if (TriggerState.NONE.equals(triggerState)) {
						return "NONE";
					} else if (TriggerState.NORMAL.equals(triggerState)) {
						return "SCHEDULED";
					}
				}
			}
		} catch (SchedulerException e) {
			logger.error("SchedulerException while checking job with name and group exist: {}", e.getMessage());
		}
		return "UNKNOWN";
	}
}