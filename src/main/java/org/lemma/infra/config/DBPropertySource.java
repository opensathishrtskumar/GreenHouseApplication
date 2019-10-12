package org.lemma.infra.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * Loads Properties from DB and sets in Environment
 * 
 * 
 * @author RTS Sathish Kumar
 *
 */
public class DBPropertySource implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(DBPropertySource.class);

	private String dbUrl;
	private ApplicationContext applicationContext;

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public Properties createDBPropertySource() {

		logger.debug("Loading Settings Properties from DB : dburl {}", dbUrl);
		ConfigurableEnvironment configEnv = applicationContext.getBean(ConfigurableEnvironment.class);

		MutablePropertySources propertySources = configEnv.getPropertySources();

		Properties dbSettingsProperties = new Properties();
		dbSettingsProperties.put("smtp.host", "smtp.gmail.com");

		propertySources.addLast(new PropertiesPropertySource("DB_SETTINGS", dbSettingsProperties));

		logger.debug("Settings Properties from DB Loaded successfully");

		return dbSettingsProperties;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
