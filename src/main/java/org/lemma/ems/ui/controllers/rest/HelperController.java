
package org.lemma.ems.ui.controllers.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemma.ems.base.cache.CacheEntryConstants;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.cache.Caches;
import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.constants.QueryConstants;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.notification.util.Mail;
import org.lemma.ems.notification.util.MailTemplateConstants;
import org.lemma.ems.notification.util.Mailer;
import org.lemma.ems.scheduler.jobs.SampleCronJob;
import org.lemma.ems.scheduler.util.JobUtil;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class HelperController {

	private static final Logger logger = LoggerFactory.getLogger(HelperController.class);

	@Autowired
	Sender sender;

	@Autowired
	SchedulerFactoryBean schedulerFactory;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Mailer mailer;

	@Autowired
	PollingDetailsDAO pollingDao;
	
	@Autowired
	CacheUtil cacheUtil;
	

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	@ResponseBody
	public DeviceDetailsDTO publish() {

		DeviceDetailsDTO dto = new DeviceDetailsDTO();
		dto.setDeviceName("EMSDevice");

		return dto;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	@ResponseBody
	public String updatePolling() {

		Map<String, List<ExtendedSerialParameter>> cacheEntry = cacheUtil.getCacheEntry(Caches.DEVICECACHE, CacheEntryConstants.DeviceEntryConstants.GROUPED_ACTIVE_DEVICES.getName(),Map.class);
		System.out.println(cacheEntry);

		return "Success";
	}

	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	@ResponseBody
	public String schedule() {

		JobDetail jobDetail = JobUtil.createJob(SampleCronJob.class, false, context, "SampleCronJob", "SampleTrigger");

		Trigger cronTriggerBean = JobUtil.createCronTrigger("triggerKey", new Date(), "0/10 * * * * ?", jobDetail,
				SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

		try {
			schedulerFactory.getScheduler().scheduleJob(jobDetail, cronTriggerBean);
		} catch (Exception e) {
			logger.error("Error scheduling tasks {}", e);
		}

		return "Success";
	}

	@RequestMapping(value = "/notification", method = RequestMethod.GET)
	@ResponseBody
	public String sendNotification() {

		Mail mail = new Mail();
		mail.setTemplateName(MailTemplateConstants.WELCOME_VM);
		mail.setMailFrom("EMS_NO_REPLY@gmail.com");
		mail.setMailTo("sathishrtskumar@gmail.com");
		mail.setMailSubject("Welcome Team");

		Map<String, Object> params = new HashMap<>(2);
		params.put("name", "Lemma");

		try {
			mailer.sendMail(mail, params);
		} catch (Exception e) {
			logger.error("Error sending email {}", e);
		}

		return "Success";
	}

}
