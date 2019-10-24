package org.lemma.infra.config.listener;

import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.ApplicationStartupListener;
import org.lemma.ems.base.mqueue.subscriber.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
public class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private Sender sender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.
	 * springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// Send notification for individual events on Application startup
		sender.publishEvent(Receiver.Topics.STARTUP.getTopic(), Receiver.Topics.STARTUP.getTopic() + " Begin ");

		sender.publishEvent(ApplicationStartupListener.Topics.LOAD_SETTINGS.getTopic(),
				ApplicationStartupListener.Topics.LOAD_SETTINGS.getTopic());

		sender.publishEvent(ApplicationStartupListener.Topics.LOAD_DEVICES.getTopic(),
				ApplicationStartupListener.Topics.LOAD_DEVICES.getTopic());

		sender.publishEvent(ApplicationStartupListener.Topics.TRIGGER_SCHEDULES.getTopic(),
				ApplicationStartupListener.Topics.TRIGGER_SCHEDULES.getTopic());

		sender.publishEvent(Receiver.Topics.STARTUP.getTopic(), Receiver.Topics.STARTUP.getTopic() + " End ");
	}
}