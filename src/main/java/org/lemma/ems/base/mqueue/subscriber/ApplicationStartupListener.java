package org.lemma.ems.base.mqueue.subscriber;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.SchedulesDAO;
import org.lemma.ems.base.dao.SettingsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.SchedulesDTO;
import org.lemma.ems.base.dao.dto.SettingsDTO;
import org.lemma.ems.scheduler.util.JobUtil;
import org.lemma.ems.service.DeviceMapper;
import org.lemma.ems.util.EMSUtility;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@EnableJms
public class ApplicationStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

	@Value("${scheduler.enabled}")
	private boolean schedulerEnabled;

	@Autowired
	private CacheUtil cacheUtil;

	@Autowired
	private SettingsDAO settingsDao;

	@Autowired
	private SchedulesDAO schedulesDAO;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private SchedulerFactoryBean schedulerFactory;

	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private DeviceDetailsDAO deviceDetailsDAO;
	
	/* constants */
	private static final String LOAD_SETTINGS_TXT = "LOAD.SETTINGS.TOPIC";
	private static final String LOAD_DEVICES_TXT = "LOAD.DEVICES.TOPIC";
	private static final String TRIGGER_SCHEDULES_TXT = "TRIGGER.SCCHEDULES.TOPIC";
	
	public enum Topics {
		LOAD_SETTINGS(LOAD_SETTINGS_TXT),
		LOAD_DEVICES(LOAD_DEVICES_TXT), 
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
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = LOAD_SETTINGS_TXT, containerFactory = "topicSubscriberConfig")
	public void loadSettings2Cache(Object message) throws Exception {
		
		List<SettingsDTO> settings = settingsDao.fetchSettings();

		Map<String, List<SettingsDTO>> collect = settings.stream()
				.collect(Collectors.groupingBy(SettingsDTO::getGroupName));

		/* Segregates by GROUP and Config keys to access easily */
		Map<String, Map<String, List<SettingsDTO>>> settingsCollection = new HashMap<>();

		for (Entry<String, List<SettingsDTO>> entry : collect.entrySet()) {

			List<SettingsDTO> value = entry.getValue();

			Map<String, List<SettingsDTO>> configElements = value.stream()
					.collect(Collectors.groupingBy(SettingsDTO::getConfigName));

			settingsCollection.put(entry.getKey(), configElements);
		}

		cacheUtil.putCacheEntry(CacheEntryConstants.EmsEntryConstants.SETTINGS.getName(), settingsCollection);

		logger.info("loadSettings2Cache Loading Settings into Cache Completed ");

		Map<String, List<SettingsDTO>> list = settingsCollection.get(SettingsDAO.SettingsGroup.EMAIL.getGroupKey());

		populateMailSenderBean(mailSender, list);

		logger.info("MailSender bean configured Completed ");

	}

	private void populateMailSenderBean(JavaMailSenderImpl mailSender, Map<String, List<SettingsDTO>> list) {

		String host = list.get(SettingsDAO.EMAILGroup.HOST.getMailKey()).get(0).getConfigValue();
		String port = list.get(SettingsDAO.EMAILGroup.PORT.getMailKey()).get(0).getConfigValue();
		String username = list.get(SettingsDAO.EMAILGroup.USERNAME.getMailKey()).get(0).getConfigValue();
		String password = list.get(SettingsDAO.EMAILGroup.PASSWORD.getMailKey()).get(0).getConfigValue();

		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));
		mailSender.setUsername(username);
		mailSender.setPassword(password);
	}

	
	/**
	 * @param message
	 */
	@JmsListener(destination = LOAD_DEVICES_TXT, containerFactory = "topicSubscriberConfig")
	public void loadDeviceDetails2Cache(Object message) {
		logger.info("loadDeviceDetails2Cache Loading DeviceDetails into Cache {}", message);

		// Load active EMS Devices and place in cache
		List<DeviceDetailsDTO> emsActiveDevices = deviceDetailsDAO.fetchEMSActiveDevices();

		// calculatePolling params for all device and its memory mappings
		for (DeviceDetailsDTO device : emsActiveDevices) {

			List<ExtendedDeviceMemoryDTO> memoryMappings = device.getMemoryMappings();

			for (ExtendedDeviceMemoryDTO memory : memoryMappings) {
				memory.calculatePollingParams();
			}
		}
		
		//1.All ems actives persisted directly
		cacheUtil.putCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.ACTIVE_DEVICES.getName(), emsActiveDevices);
		
		//2.All ems actives are converted to ExtendedSerialParameter and persisted as group for better polling
		List<ExtendedSerialParameter> mapDevicesToSerialParams = DeviceMapper.mapDevicesToSerialParams(emsActiveDevices);
		Map<String, List<ExtendedSerialParameter>> groupDevicesForPolling = EMSUtility.groupDevicesForPolling(mapDevicesToSerialParams);
		cacheUtil.putCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.GROUPED_ACTIVE_DEVICES.getName(), groupDevicesForPolling);
		
		logger.info("LoadDeviceDetails2Cache Loading DeviceDetails done {}", message);
	}

	
	/**
	 * When {@link scheduler.enabled} true 1. loads all schedules 2. Active
	 * schedules are scheduled / rescheduled 3. Inactive schedule are unscheduled /
	 * stopped
	 * 
	 * @param message
	 */
	@JmsListener(destination = TRIGGER_SCHEDULES_TXT, containerFactory = "topicSubscriberConfig")
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