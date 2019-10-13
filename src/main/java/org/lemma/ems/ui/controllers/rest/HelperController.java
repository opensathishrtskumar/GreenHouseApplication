
package org.lemma.ems.ui.controllers.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.constants.QueryConstants;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.base.mqueue.publisher.Sender;
import org.lemma.ems.base.mqueue.subscriber.ApplicationStartupListener;
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

	@Autowired
	private Mailer mailer;

	@Autowired
	PollingDetailsDAO pollingDao;

	@Value("${startup.notification}")
	private String startupNotification;

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	@ResponseBody
	public DeviceDetailsDTO publish() {

		DeviceDetailsDTO dto = new DeviceDetailsDTO();
		dto.setDeviceName("EMSDevice");

		sender.publishEvent(ApplicationStartupListener.APP_STARTUP, "SUCCESS");

		return dto;
	}

	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	@ResponseBody
	public PollingDetailsDTO updatePolling() {

		PollingDetailsDTO dtopoll = new PollingDetailsDTO();
		dtopoll.setDeviceUniqueId(2);
		dtopoll.setPolledOn(123456789);
		dtopoll.setVoltage_bn(123.11);
		dtopoll.setVoltage_br(125.22);
		dtopoll.setVoltage_rn(127.33);
		dtopoll.setVoltage_ry(129.44);
		dtopoll.setVoltage_yb(131.55);
		dtopoll.setVoltage_yn(133.66);
		dtopoll.setVoltage_avg_ll(135.77);
		dtopoll.setVoltage_avg_ln(137.88);
		dtopoll.setR_current(139.99);
		dtopoll.setY_current(142.1);
		dtopoll.setB_current(144.21);
		dtopoll.setCurrent_avg(146.32);
		dtopoll.setFrequency(148.43);
		dtopoll.setPower_factor(150.54);
		dtopoll.setW1(152.65);
		dtopoll.setW2(154.76);
		dtopoll.setW3(156.87);
		dtopoll.setWh(158.98);
		dtopoll.setW_avg(161.09);
		dtopoll.setVa1(163.2);
		dtopoll.setVa2(165.31);
		dtopoll.setVa3(167.42);
		dtopoll.setVah(169.53);
		dtopoll.setVa_avg(171.64);

		int queryUpStatus = pollingDao.execute(QueryConstants.INSERT_POLLING_DETAILS,
				new Object[] { dtopoll.getDeviceUniqueId(), dtopoll.getPolledOn(), dtopoll.getVoltage_bn(),
						dtopoll.getVoltage_br(), dtopoll.getVoltage_rn(), dtopoll.getVoltage_ry(),
						dtopoll.getVoltage_yb(), dtopoll.getVoltage_yn(), dtopoll.getVoltage_avg_ll(),
						dtopoll.getVoltage_avg_ln(), dtopoll.getR_current(), dtopoll.getY_current(),
						dtopoll.getB_current(), dtopoll.getCurrent_avg(), dtopoll.getFrequency(),
						dtopoll.getPower_factor(), dtopoll.getW1(), dtopoll.getW2(), dtopoll.getW3(), dtopoll.getWh(),
						dtopoll.getW_avg(), dtopoll.getVa1(), dtopoll.getVa2(), dtopoll.getVa3(), dtopoll.getVah(),
						dtopoll.getVa_avg() });

		return dtopoll;
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
