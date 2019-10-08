package org.lemma.ems.base.mqueue.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish  Kumar
 *
 */
@Component
public class Sender {

	@Autowired
	@Qualifier("publisherTemplate")
	private JmsTemplate senderTemplate;

	/**
	 * @param topic
	 * @param message
	 */
	public void publishEvent(String topic, Object message) {
		senderTemplate.convertAndSend(topic, message);
	}

}
