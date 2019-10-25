package org.lemma.ems.base.mqueue.subscriber;

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
import org.lemma.ems.base.dao.SettingsDAO;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.SettingsDTO;
import org.lemma.ems.base.mqueue.ReceiverConfig;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.service.DeviceMapper;
import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@EnableJms
public class ApplicationStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

	@Autowired
	private CacheUtil cacheUtil;

	@Autowired
	private SettingsDAO settingsDao;

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private Sender sender;

	@Autowired
	private DeviceDetailsDAO deviceDetailsDAO;

	/* constants */
	private static final String LOAD_SETTINGS_TXT = "LOAD.SETTINGS.TOPIC";
	private static final String LOAD_DEVICES_TXT = "LOAD.DEVICES.TOPIC";

	public enum Topics {
		LOAD_SETTINGS(LOAD_SETTINGS_TXT), LOAD_DEVICES(LOAD_DEVICES_TXT);
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
	@JmsListener(destination = LOAD_SETTINGS_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
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

		// Load Devices once Settings are loaded succcessfully
		sender.publishEvent(ApplicationStartupListener.Topics.LOAD_DEVICES.getTopic(),
				ApplicationStartupListener.Topics.LOAD_DEVICES.getTopic());

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
	@JmsListener(destination = LOAD_DEVICES_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
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

		// 1.All ems actives persisted directly
		cacheUtil.putCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.ACTIVE_DEVICES.getName(),
				emsActiveDevices);

		// 2.All ems actives are converted to ExtendedSerialParameter and persisted as
		// group for better polling
		List<ExtendedSerialParameter> mapDevicesToSerialParams = DeviceMapper
				.mapDevicesToSerialParams(emsActiveDevices);
		Map<String, List<ExtendedSerialParameter>> groupDevicesForPolling = EMSUtility
				.groupDevicesForPolling(mapDevicesToSerialParams);
		cacheUtil.putCacheEntry(Caches.DEVICECACHE,
				CacheEntryConstants.DeviceEntryConstants.GROUPED_ACTIVE_DEVICES.getName(), groupDevicesForPolling);

		logger.info("LoadDeviceDetails2Cache Loading DeviceDetails done {}", message);

		// Trigger schedules once devices are loaded
		sender.publishEvent(SchedulerStartupListener.Topics.TRIGGER_SCHEDULES.getTopic(),
				SchedulerStartupListener.Topics.TRIGGER_SCHEDULES.getTopic());
	}
}