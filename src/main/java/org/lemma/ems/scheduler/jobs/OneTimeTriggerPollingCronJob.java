package org.lemma.ems.scheduler.jobs;

import static org.lemma.ems.base.core.constants.Core.SERIAL_MUTEX;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.core.EMSDeviceConnectionManager;
import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.core.ResponseHandler;
import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.PollingListener;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.ModbusUtil;

/**
 * @author RTS Sathish Kumar
 *
 *         Job will be executed only once, will keep running forever as
 *         configured
 */
public class OneTimeTriggerPollingCronJob extends QuartzJobBean implements InterruptableJob {

	private static final Logger logger = LoggerFactory.getLogger(OneTimeTriggerPollingCronJob.class);

	@Autowired
	Environment environmment;

	@Autowired
	CacheUtil cacheUtil;

	@Autowired
	Sender sender;

	boolean polling = true;

	/**
	 * 
	 */
	private static final int MAX_REFRESH_COUNTER = 500;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.scheduling.quartz.QuartzJobBean#executeInternal(org.
	 * quartz.JobExecutionContext)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		int refreshCounter = 0;
		// Get grouped device details for polling
		Map<String, List<ExtendedSerialParameter>> cacheEntry = cacheUtil.getCacheEntry(Caches.DEVICECACHE,
				CacheEntryConstants.DeviceEntryConstants.GROUPED_ACTIVE_DEVICES.getName(), Map.class);

		// Delay between every Polling cycle, load from Settings
		long delay = 10000;

		EMSDeviceConnectionManager connectionManager = new EMSDeviceConnectionManager();

		// Keep thread running until interrupted
		while (polling) {
			
			logger.trace(" Polling device Job ");
			
			/*
			 * 1. Refresh device details from cache to pick newly added devices
			 * automatically 2. Get Polling delay from settings
			 */
			if (refreshCounter >= MAX_REFRESH_COUNTER) {
				cacheEntry = cacheUtil.getCacheEntry(Caches.DEVICECACHE,
						CacheEntryConstants.DeviceEntryConstants.GROUPED_ACTIVE_DEVICES.getName(), Map.class);

				// TODO: from settings
				delay = 30000;
			}

			pollGroup(cacheEntry, connectionManager);

			refreshCounter += 1;

			ModbusUtil.sleep(delay);
		}
	}

	/**
	 * @param cacheEntry
	 * @param connectionManager
	 */
	private void pollGroup(Map<String, List<ExtendedSerialParameter>> cacheEntry,
			EMSDeviceConnectionManager connectionManager) {
		for (Entry<String, List<ExtendedSerialParameter>> groupEntry : cacheEntry.entrySet()) {

			// When entry is available, there is an entry in Specific group
			List<ExtendedSerialParameter> groupedDevices = groupEntry.getValue();

			// Create SerialConnection for given group only once
			ExtendedSerialParameter connectionParam = groupedDevices.get(0);

			SerialConnection serialConnection = null;

			/*
			 * When failed to get connection, proceed with next group
			 */
			try {
				serialConnection = Core.getSerialConnection(connectionParam);
			} catch (Exception e) {
				sender.publishEvent(PollingListener.Topics.FAILED_CONNECTION_GROUP.getTopic(), connectionParam);
				continue;
			}

			// Create transaction, execte request and put response in queue for processing
			synchronized (SERIAL_MUTEX) {
				pollDevices(connectionManager, groupedDevices, serialConnection);
			}
		}
	}

	/**
	 * @param connectionManager
	 * @param groupedDevices
	 * @param serialConnection
	 */
	private void pollDevices(EMSDeviceConnectionManager connectionManager, List<ExtendedSerialParameter> groupedDevices,
			SerialConnection serialConnection) {
		for (ExtendedSerialParameter device : groupedDevices) {

			try {
				ResponseHandler executeTransaction = connectionManager.executeTransaction(serialConnection, device);

				if (!executeTransaction.isError()) {
					sender.publishEvent(PollingListener.Topics.POLLING_COMPLETED.getTopic(), executeTransaction);
				} else {
					sender.publishEvent(PollingListener.Topics.FAILED_DEVICE.getTopic(), device);
				}
			} catch (Exception e) {
				logger.error(" Failed to connect with device : {} with error {} ", device.getUniqueId(), e);
				sender.publishEvent(PollingListener.Topics.FAILED_DEVICE.getTopic(), device);
			}
		}
	}

	@Override
	public void interrupt() throws UnableToInterruptJobException {
		logger.debug("interrupted EMS Polling JOB... ");
		polling = false;
	}
}