package org.lemma.ems.base.mqueue.subscriber;

import org.lemma.ems.base.mqueue.ReceiverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	@JmsListener(destination = STARTUP_TXT, containerFactory = ReceiverConfig.SUBSCRIBER_NAME)
	public void sampleReceiver1(String message) {
		LOGGER.info("'subscriber1' received message='{}' time={}", message, System.currentTimeMillis());
	}
}