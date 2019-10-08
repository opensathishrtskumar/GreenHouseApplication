package org.lemma.ems.base.mqueue.subscriber;

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

	@JmsListener(destination = "${startup.notification}", containerFactory = "topicSubscriberConfig")
	public void receive1(String message) {
		LOGGER.info("'subscriber1' received message='{}' time={}", message, System.currentTimeMillis());
	}

	@JmsListener(destination = "${startup.notification}", containerFactory = "topicSubscriberConfig")
	public void receive2(String message) {
		LOGGER.info("'subscriber2' received message='{}' time={}", message, System.currentTimeMillis());
	}
}