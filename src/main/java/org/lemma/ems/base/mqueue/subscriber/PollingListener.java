package org.lemma.ems.base.mqueue.subscriber;

import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
	private ApplicationContext context;

	@Autowired
	PollingDetailsDAO pollingDao;

	/* constants */
	private static final String PERSIST_DB_POLLING_TXT = "PERSIST_POLLING.RESPONSE.TOPIC";

	public enum Topics {
		PERSIST_POLLING_RESPONSE(PERSIST_DB_POLLING_TXT);
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
	@JmsListener(destination = PERSIST_DB_POLLING_TXT, containerFactory = "topicSubscriberConfig")
	public void persistResponseinDB(PollingDetailsDTO dtopoll) throws Exception {
		pollingDao.insertPollingDetails(dtopoll);
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	@JmsListener(destination = PERSIST_DB_POLLING_TXT, containerFactory = "topicSubscriberConfig")
	public void persistResponseinCache(PollingDetailsDTO dtopoll) throws Exception {
		//TODO : update latest value in cache, Keep last 10 records. Dashboard live monitoring and other usage
	}

}