package org.lemma.ems.base.mqueue.subscriber;

import java.util.Date;
import java.util.List;

import org.lemma.ems.base.dao.SchedulesDAO;
import org.lemma.ems.base.dao.dto.SchedulesDTO;
import org.lemma.ems.base.mqueue.ReceiverConfig;
import org.lemma.ems.scheduler.util.JobUtil;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
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
	private static final String TRIGGER_SCHEDULES_TXT = "TRIGGER.SCCHEDULES.TOPIC";

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

					JobDetail jobDetail = JobUtil.createJob(forName, false, context, schedule.getJobKey(),
							schedule.getGroupKey());

					Trigger cronTriggerBean = JobUtil.createCronTrigger(schedule.getJobKey() + schedule.getGroupKey(),
							new Date(), schedule.getCronExpression(), jobDetail,
							SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

					/*
					 * Get job existance status - unschedule all task and schedule if status is
					 * active
					 */
					boolean checkExists = schedulerFactory.getScheduler().checkExists(triggerKey);

					logger.debug("{} job existane {}", schedule.getClassName(), checkExists);

					if (checkExists) {
						schedulerFactory.getScheduler().unscheduleJob(triggerKey);
						logger.debug("{} job unscheduled", schedule.getClassName());
					}

					if (schedule.getStatus() == SchedulesDAO.ACTIVE) {
						schedulerFactory.getScheduler().scheduleJob(jobDetail, cronTriggerBean);
						logger.debug("{} job scheduled", schedule.getClassName());
					}

				} catch (Exception e) {
					logger.error("Error scheduling tasks {} {}", schedule.getClassName(), e);
				}
			}
		}
	}
}