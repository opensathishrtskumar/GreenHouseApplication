package org.lemma.ems.base.mqueue.subscriber;

import org.lemma.ems.base.cache.CacheUtil;
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

	public enum Topics {
		PERSIST_POLLING_RESPONSE(PERSIST_DB_POLLING_TXT), POLLING_COMPLETED(
				POLLING_COMPLETED_TXT), FAILED_CONNECTION_GROUP(
						FAILED_CONNECTION_GROUP_TXT), FAILED_DEVICE(FAILED_DEVICE_TXT);
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
		// TODO : update latest value in cache, Keep last 10 records. Dashboard live
		// monitoring and other usage
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = POLLING_COMPLETED_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void parsePollingResponse(EMSDeviceResponseHolder dtopoll) throws Exception {
		PollingDetailsDTO dto = EMSUtility.populatePollingValuesToDto(dtopoll);
		sender.publishEvent(Topics.PERSIST_POLLING_RESPONSE.getTopic(), dto);
	}

}