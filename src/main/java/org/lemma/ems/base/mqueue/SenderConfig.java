package org.lemma.ems.base.mqueue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class SenderConfig {

	@Value("${activemq.vm-broker-url}")
	private String brokerUrl;

	@Bean(name = "publisherConfig")
	public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setAlwaysSyncSend(false);
		activeMQConnectionFactory.setAlwaysSessionAsync(true);
		activeMQConnectionFactory.setDispatchAsync(true);
		activeMQConnectionFactory.setDisableTimeStampsByDefault(true);
		activeMQConnectionFactory.setUseAsyncSend(true);
		activeMQConnectionFactory.setMaxThreadPoolSize(10);
		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(senderActiveMQConnectionFactory());
	}

	@Bean(name = "topicPublisherTemplate")
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}
}