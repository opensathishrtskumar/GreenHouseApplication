package org.lemma.infra.config.listener;

import org.lemma.ems.scheduler.jobs.SampleCronJob;

public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		String clsName = SampleCronJob.class.getCanonicalName();
		
		Class<?> forName = Class.forName(clsName);
		System.out.println(SampleCronJob.class.getCanonicalName());
	}
	
}
