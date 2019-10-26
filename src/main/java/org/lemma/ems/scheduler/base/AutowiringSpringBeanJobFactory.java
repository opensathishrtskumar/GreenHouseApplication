package org.lemma.ems.scheduler.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lemma.ems.scheduler.jobs.SimpleStoppableJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

/**
 * @author RTS Sathish Kumar
 *
 */
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory beanFactory;

	private static final Map<String, List<Object>> STOPPABLEJOBS = new ConcurrentHashMap<>();

	@Override
	public void setApplicationContext(final ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		final Object job = super.createJobInstance(bundle);
		handleStoppableJob(bundle.getJobDetail(), job);
		beanFactory.autowireBean(job);
		return job;
	}

	/**
	 * @param jobDetail
	 * @param job
	 */
	private static void handleStoppableJob(JobDetail jobDetail, Object job) {
		if (job instanceof SimpleStoppableJob) {
			String key = jobDetail.getKey().toString();
			List<Object> list = STOPPABLEJOBS.get(key);

			if (list == null)
				list = new ArrayList<>();

			list.add(job);

			STOPPABLEJOBS.put(jobDetail.getKey().toString(), list);
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static List<Object> getSimpleStoppableJob(JobKey key) {
		return STOPPABLEJOBS.get(key.toString());
	}

	/**
	 * @param key
	 */
	public static void removeSimpleStoppableJob(JobKey key) {
		STOPPABLEJOBS.remove(key.toString());
	}
}