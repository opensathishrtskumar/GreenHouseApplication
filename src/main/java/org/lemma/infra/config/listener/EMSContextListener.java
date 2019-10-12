package org.lemma.infra.config.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.ApplicationStartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class EMSContextListener
		implements ServletContextListener, ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	private static final Logger logger = LoggerFactory.getLogger(EMSContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("context initialized...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("context destroyed...");
	}

	@Override
	public void initialize(ConfigurableWebApplicationContext applicationContext) {
		Sender bean = applicationContext.getBean(Sender.class);

		bean.publishEvent(ApplicationStartupListener.APP_STARTUP, "APPLICATION_STARTED");
	}
}
