package org.lemma.ems.UI.controllers.rest;

import java.util.Date;

import org.lemma.ems.UI.dto.DeviceDetailsDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.scheduler.jobs.SampleCronJob;
import org.lemma.ems.scheduler.util.JobUtil;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${startup.notification}")
	private String startupNotification;

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	@ResponseBody
	public DeviceDetailsDTO publish() {

		sender.publishEvent(startupNotification, "SUCCESS");

		DeviceDetailsDTO dto = new DeviceDetailsDTO();
		dto.setDeviceName("EMSDevice");

		return dto;
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
			e.printStackTrace();
		}

		return "Success";
	}
}
