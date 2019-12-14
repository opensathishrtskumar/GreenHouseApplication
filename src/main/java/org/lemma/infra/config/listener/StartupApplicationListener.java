package org.lemma.infra.config.listener;

import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.DeviceSettingsListener;
import org.lemma.ems.base.mqueue.subscriber.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(StartupApplicationListener.class);

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

		// Events invoked more than once, i.e for FrameworkServlet and WebAppRootContext
		XmlWebApplicationContext context = ((XmlWebApplicationContext) event.getSource());

		logger.info(" Event --> {} ", context);

		if (context.getDisplayName().contains("-servlet")) {
			logger.info(" triggers Application Initialize notification for {} ", context.getNamespace());

			// Send notification for individual events on Application startup
			sender.publishEvent(DeviceSettingsListener.Topics.LOAD_SETTINGS.getTopic(),
					DeviceSettingsListener.Topics.LOAD_SETTINGS.getTopic());
			
			sender.publishEvent(Receiver.Topics.STARTUP.getTopic(),
					Receiver.Topics.STARTUP.getTopic());
		}
	}
}