package org.lemma.ems.config.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EMSContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(EMSContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.debug("context initialized...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.debug("context destroyed...");
	}
}
