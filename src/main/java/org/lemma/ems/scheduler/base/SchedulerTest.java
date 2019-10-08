package org.lemma.ems.scheduler.base;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerTest {

	public static void main(String[] args) throws Exception {
		
		String groupName = "Group1";
		
		JobKey jobKeyA = new JobKey("jobC", groupName);
		JobDetail jobA = JobBuilder.newJob(JobC.class).withIdentity(jobKeyA).build();

		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("dummyTriggerName1", groupName)
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
		
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getListenerManager().addJobListener(new JobListerImpl());

		scheduler.start();
		scheduler.scheduleJob(jobA, trigger1);
		
		scheduler.unscheduleJob(TriggerKey.triggerKey("jobC", groupName));
	}

	public static class JobC implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			System.out.println("Job C is runing");
		}

	}

}

