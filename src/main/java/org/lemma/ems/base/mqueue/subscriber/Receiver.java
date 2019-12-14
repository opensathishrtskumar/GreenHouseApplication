package org.lemma.ems.base.mqueue.subscriber;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.mqueue.ReceiverConfig;
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
public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	/* constants */
	private static final String STARTUP_TXT = "STARTUP.TOPIC";
	
	@Autowired
	private CacheUtil cacheUtil;
	

	public enum Topics {
		STARTUP(STARTUP_TXT);
		String topic;

		private Topics(String topic) {
			this.topic = topic;
		}

		public String getTopic() {
			return topic;
		}
	}

	/**
	 * Initializes required cache entries with empty objects
	 * 
	 * @param message
	 */
	@JmsListener(destination = STARTUP_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void initializeCacheWithEmpty(String message) {
		LOGGER.info("'subscriber1' received message='{}' time={}", message, System.currentTimeMillis());
		
		//1. Device states map to hold last N entries
		Map<Long, CircularFifoQueue<PollingDetailsDTO>> deviceStates = new HashMap<>();
		cacheUtil.putCacheEntry(Caches.DEVICECACHE, 
				CacheEntryConstants.DeviceEntryConstants.DEVICE_STATES.getName(),  deviceStates);
		
	}
}