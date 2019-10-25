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
import org.lemma.ems.scheduler.util.JobUtil;
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
		logger.info("triggerSchedules Scheduler status {}", schedulerEnabled);

		/* Trigger schedles only when enabled */
		if (schedulerEnabled) {

			List<SchedulesDTO> activeSchedules = schedulesDAO.fetchAllSchedules();

			for (SchedulesDTO schedule : activeSchedules) {
				try {
					Class<QuartzJobBean> forName = (Class<QuartzJobBean>) Class.forName(schedule.getClassName());

					TriggerKey triggerKey = TriggerKey.triggerKey(schedule.getJobKey() + schedule.getGroupKey());
					JobDetail jobDetail = createJob(schedule, forName, context);

					/*
					 * Get job existance status - unschedule all task and reschedule if status is
					 * active
					 */
					boolean checkExists = schedulerFactory.getScheduler().checkExists(triggerKey);

					logger.debug("{} job existane {}", schedule.getClassName(), checkExists);

					if (checkExists) {
						schedulerFactory.getScheduler().unscheduleJob(triggerKey);
						//FIXME: unable to delete/unscchedle onetime jobs check
						schedulerFactory.getScheduler()
								.deleteJob(JobKey.jobKey(schedule.getJobKey(), schedule.getGroupKey()));
						logger.debug("{} job unscheduled", schedule.getClassName());
					}

					if (schedule.getStatus() == SchedulesDAO.Status.ACTIVE.getStatus()) {
						Trigger cronTriggerBean = createTrigger(schedule, jobDetail);
						schedulerFactory.getScheduler().scheduleJob(jobDetail, cronTriggerBean);
						logger.debug("{} job scheduled", schedule.getClassName());
					} else if (schedule.getStatus() == SchedulesDAO.Status.ONETIMEJOB.getStatus()) {
						scheduleOneTimeJob(schedule, new Date());
					}

				} catch (Exception e) {
					logger.error("Error scheduling tasks {} {}", schedule.getClassName(), e);
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
			Class<QuartzJobBean> forName = (Class<QuartzJobBean>) Class.forName(schedule.getClassName());
			TriggerKey triggerKey = TriggerKey.triggerKey(schedule.getJobKey() + schedule.getGroupKey());
			JobDetail jobDetail = createJob(schedule, forName, context);

			logger.debug("creating trigger for key : {}", triggerKey.toString());
			Trigger cronTriggerBean = JobUtil.createSingleTrigger(triggerKey.toString(), date,
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW, jobDetail);

			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.unscheduleJob(triggerKey);

			scheduler.scheduleJob(jobDetail, cronTriggerBean);
			logger.debug("Job with key {} scheduled successfully", triggerKey.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Scheduler Excception Job with key {} failed {}", schedule.getClassName(), e);
		}

		return false;
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
						String jobState = getJobState(jobName,jobGroup);
						map.put("jobStatus", jobState);
					}

					list.add(map);
					logger.debug("Job details:");
				}

			}
		} catch (SchedulerException e) {
			logger.error("SchedulerException while fetching all jobs. error message :" + e.getMessage());
		}
		return list;
	}

	/**
	 * Check if job is already running
	 */
	private boolean isJobRunning(String jobName, String groupName) {
		logger.debug("Request received to check if job is running");

		String jobKey = jobName;
		String groupKey = groupName;

		logger.debug("Parameters received for checking job is running now : jobKey :" + jobKey);
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
			String groupKey = groupName;
			JobKey jobKey = new JobKey(jobName, groupKey);

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
			logger.error("SchedulerException while checking job with name and group exist:" + e.getMessage());
		}
		return null;
	}
}