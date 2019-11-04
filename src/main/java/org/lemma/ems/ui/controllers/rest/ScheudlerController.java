
package org.lemma.ems.ui.controllers.rest;

import java.util.List;
import java.util.Map;

import org.lemma.ems.base.mqueue.subscriber.SchedulerStartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RTS Sathish Kumar
 *
 */
@RestController
@RequestMapping("/rest")

public class ScheudlerController {

	private static final Logger logger = LoggerFactory.getLogger(ScheudlerController.class);

	@Autowired
	SchedulerStartupListener schedules;

	/**
	 * @return
	 */
	@RequestMapping(value = "/list/schedules", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> publish() {
		return schedules.getAllJobs();
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/jobs/reschedule", method = RequestMethod.GET)
	@ResponseBody
	public String rescuedule() {
		schedules.triggerSchedules("RESCHEDULE");
		return "RESCHEDULED";
	}
}
