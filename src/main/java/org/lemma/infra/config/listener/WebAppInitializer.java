package org.lemma.infra.config.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.lemma.infra.config.DBConfigPropertySourceInitializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.WebApplicationInitializer;

@Component
public class WebAppInitializer implements WebApplicationInitializer {

	private static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		String initializerClasses = servletContext.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);

		String propertySourceInitClassName = DBConfigPropertySourceInitializer.class.getName();

		if (StringUtils.hasText(initializerClasses)) {
			initializerClasses += " " + propertySourceInitClassName;
		} else {
			initializerClasses = propertySourceInitClassName;
		}

		servletContext.setInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM, initializerClasses);
	}
}