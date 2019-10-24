package org.lemma.infra.config.listener;

import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.ApplicationStartupListener;
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

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		sender.publishEvent(ApplicationStartupListener.APP_STARTUP, "APPLICATION_STARTED");
	}
}