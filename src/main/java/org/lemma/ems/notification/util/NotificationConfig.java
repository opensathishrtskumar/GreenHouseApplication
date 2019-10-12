package org.lemma.ems.notification.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

/**
 * @author RTS Sathish Kumar
 *
 */
@Configuration
public class NotificationConfig {

	private static final Logger logger = LoggerFactory.getLogger(NotificationConfig.class);

	/**
	 * @return
	 * @throws VelocityException
	 * @throws IOException
	 */
	@Bean
	public VelocityEngine createVelocityEngineFactoryBean() throws VelocityException, IOException {
		VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		bean.setVelocityProperties(props);
		bean.afterPropertiesSet();
		return bean.createVelocityEngine();
	}

	@Bean
	@Lazy(true)
	public JavaMailSenderImpl createJavaMailSenderImpl(/*@Value("${smtp.host}") String host*/) {
		JavaMailSenderImpl bean = new JavaMailSenderImpl();

		//logger.debug(" host {}", host);

		// FIXME : load from props or settings
		bean.setHost("smtp.gmail.com");
		bean.setPort(587);
		bean.setUsername("ems.ses03@gmail.com");
		bean.setPassword("kavi071215");

		Properties javaMailProps = new Properties();
		javaMailProps.put("mail.debug", true);
		javaMailProps.put("mail.smtp.auth", true);
		javaMailProps.put("mail.smtp.starttls.enable", true);
		javaMailProps.put("mail.mime.charset", "UTF-8");
		javaMailProps.put("mail.transport.protocol", "smtp");

		bean.setJavaMailProperties(javaMailProps);

		return bean;
	}

}
