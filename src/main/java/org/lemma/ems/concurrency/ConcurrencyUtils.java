package org.lemma.ems.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Singleton
public class ConcurrencyUtils implements DisposableBean{
	
	private static final Logger logger = LoggerFactory.getLogger(ConcurrencyUtils.class);

	@Autowired
	@Qualifier("taskExecutor")
	private ThreadPoolTaskExecutor workers;

	public ThreadPoolTaskExecutor getWorkers() {
		return workers;
	}

	public Future<Object> execute(Callable<Object> work) {
		return workers.submit(work);
	}

	@Override
	public void destroy() throws Exception {
		workers.shutdown();
	}
}
