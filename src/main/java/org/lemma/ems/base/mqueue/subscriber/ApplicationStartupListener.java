package org.lemma.ems.base.mqueue.subscriber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lemma.ems.UI.dto.SettingsDTO;
import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.dao.SetttingsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@EnableJms
public class ApplicationStartupListener {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

	@Autowired
	private CacheUtil cacheUtil;
	
	@Autowired
	private SetttingsDAO settingsDao;
	

	/* constants */
	public static final String APP_STARTUP = "APP.INIT.TOPIC";

	@JmsListener(destination = APP_STARTUP, containerFactory = "topicSubscriberConfig")
	public void loadSettings2Cache(Object message) throws Exception {
		List<SettingsDTO> settings = settingsDao.fetchSettings();
		
		Map<String, List<SettingsDTO>> collect = settings.stream().collect(Collectors.groupingBy(SettingsDTO::getGroupName));
		
		/*FIXME : 
		 * 1. validate hashkey of settings  
		 * 2. decrypt settings
		 * 3. JavaMailSenderImpl - set host,port,username and password
		 */
		cacheUtil.putCacheEntry(CacheEntryConstants.SETTINGS.getName(), collect);
		
		logger.info("EventListener 1 Loading Settings into Cache {}", message);
	}

	@JmsListener(destination = APP_STARTUP, containerFactory = "topicSubscriberConfig")
	public void applicationStartupEvent2(Object message) {
		logger.info("EventListener 2 Loading Settings into Cache {}", message);
	}
}