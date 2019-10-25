package org.lemma.infra.config.listener;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.constants.Core.MemoryMapping;
import org.quartz.TriggerKey;

public class MainMaster {

	public static void main(String[] args) throws Exception {
		
		TriggerKey triggerKey = TriggerKey.triggerKey("jk","gk");
		
		System.out.println(triggerKey.toString());
		
	}

}
