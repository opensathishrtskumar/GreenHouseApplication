package org.lemma.ems.base.mqueue.subscriber;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.core.EMSDeviceResponseHolder;
import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.mqueue.ReceiverConfig;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.notification.util.Mailer;
import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@EnableJms
public class PollingListener {

	private static final Logger logger = LoggerFactory.getLogger(PollingListener.class);

	@Autowired
	private CacheUtil cacheUtil;

	@Autowired
	private Sender sender;

	@Autowired
	PollingDetailsDAO pollingDao;

	@Autowired
	Mailer mailer;

	/* constants */
	private static final String PERSIST_DB_POLLING_TXT = "PERSIST.POLLING.RESPONSE.TOPIC";
	private static final String POLLING_COMPLETED_TXT = "POLLING.COMPLETED.TOPIC";
	private static final String FAILED_CONNECTION_GROUP_TXT = "FAILED.CONNECTION.GROUP.TOPIC";

	private static final String FAILED_DEVICE_TXT = "FAILED.DEVICE.TOPIC";
	private static final int NO_OF_STATES_To_KEEP = 10;

	public enum Topics {
		PERSIST_POLLING_RESPONSE(PERSIST_DB_POLLING_TXT), 
		POLLING_COMPLETED(POLLING_COMPLETED_TXT), 
		FAILED_CONNECTION_GROUP(FAILED_CONNECTION_GROUP_TXT), 
		FAILED_DEVICE(FAILED_DEVICE_TXT);
		String topic;

		private Topics(String topic) {
			this.topic = topic;
		}

		public String getTopic() {
			return topic;
		}
	}

	/**Failed devices handling begins*/
	
	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = FAILED_DEVICE_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void devicePollingFailure(ExtendedSerialParameter device) throws Exception {
		// TODO : Update cache and send notification, logic to handle continuous failure
		// 1. First failure send notification
		// 2. Every 50 failure send notification to avoid unnecessary mail flooding
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = FAILED_CONNECTION_GROUP_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void serialConnectionFailureListener(ExtendedSerialParameter connectionParam) throws Exception {
		// TODO : Send notification with required details - velocity template
		// mailer.sendMail - check HelperController
	}

	/**Failed devices handling ends*/
	
	
	/** Persist polling details in DB / Cache begins */
	
	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = PERSIST_DB_POLLING_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void persistResponseinDB(PollingDetailsDTO dtopoll) throws Exception {
		pollingDao.insertPollingDetails(dtopoll);
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = PERSIST_DB_POLLING_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void persistResponseinCache(PollingDetailsDTO dtopoll) throws Exception {
		/** update latest value in cache, Keep recent N records. Dashboard live monitoring and other usage */
		Map<Long, CircularFifoQueue<PollingDetailsDTO>> cacheEntry = (Map<Long, CircularFifoQueue<PollingDetailsDTO>>)cacheUtil
				.getCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.DEVICE_STATES.getName());
		
		CircularFifoQueue<PollingDetailsDTO> circularFifoQueue = cacheEntry.get(dtopoll.getUniqueId());
		
		//Create Queue instance and put in Cache when null
		if(circularFifoQueue == null) {
			circularFifoQueue = new CircularFifoQueue<>(NO_OF_STATES_To_KEEP);
			cacheEntry.put(dtopoll.getUniqueId(), circularFifoQueue);
		}
		
		//Add device state against given deviceid
		circularFifoQueue.add(dtopoll);
		
		cacheUtil.putCacheEntry(Caches.DEVICECACHE, 
				CacheEntryConstants.DeviceEntryConstants.DEVICE_STATES.getName(),  cacheEntry);
	}
	
	/** Persist polling details in DB / Cache ends */

	
	
	
	/** converts polled response to Polling detail begins*/
	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = POLLING_COMPLETED_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void parsePollingResponse(EMSDeviceResponseHolder dtopoll) throws Exception {
		PollingDetailsDTO dto = EMSUtility.populatePollingValuesToDto(dtopoll);
		sender.publishEvent(Topics.PERSIST_POLLING_RESPONSE.getTopic(), dto);
	}

	/** converts polled response to Polling detail ends*/
}