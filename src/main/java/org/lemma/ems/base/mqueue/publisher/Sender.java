package org.lemma.ems.base.mqueue.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

	@Autowired
	@Qualifier("publisherTemplate")
	private JmsTemplate senderTemplate;

	public void publishEvent(String topic, Object message) {
		senderTemplate.convertAndSend(topic, message);
	}

}
