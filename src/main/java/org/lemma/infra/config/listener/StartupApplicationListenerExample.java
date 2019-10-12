package org.lemma.infra.config.listener;

import org.apache.log4j.Logger;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.ApplicationStartupListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish  Kumar
 *
 */
@Component
public class StartupApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = Logger.getLogger(StartupApplicationListenerExample.class);

	@Autowired
	private Sender sender;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		sender.publishEvent(ApplicationStartupListener.APP_STARTUP, "APPLICATION_STARTED");
	}
}