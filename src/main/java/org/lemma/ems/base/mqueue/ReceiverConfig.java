package org.lemma.ems.base.mqueue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

/**
 * @author RTS Sathish  Kumar
 *
 */
@Configuration
@EnableJms
public class ReceiverConfig {

	/**
	 * vm connector gives better performance since workds inside the same VM
	 */
	@Value("${activemq.vm-broker-url}")
	private String brokerUrl;

	/**
	 * @return
	 */
	@Bean
	public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		activeMQConnectionFactory.setAlwaysSyncSend(false);
		activeMQConnectionFactory.setAlwaysSessionAsync(true);
		activeMQConnectionFactory.setDispatchAsync(true);
		activeMQConnectionFactory.setDisableTimeStampsByDefault(true);
		activeMQConnectionFactory.setUseAsyncSend(true);
		activeMQConnectionFactory.setMaxThreadPoolSize(50);
		activeMQConnectionFactory.setTrustAllPackages(true);
		return activeMQConnectionFactory;
	}

	/**
	 * @return
	 */
	@Bean(name = "topicSubscriberConfig")
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setSessionAcknowledgeMode(javax.jms.Session.DUPS_OK_ACKNOWLEDGE);
		factory.setConnectionFactory(receiverActiveMQConnectionFactory());
		factory.setPubSubDomain(true);
		return factory;
	}
}