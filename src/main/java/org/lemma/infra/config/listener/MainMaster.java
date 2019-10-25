package org.lemma.infra.config.listener;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.constants.Core.MemoryMapping;

public class MainMaster {

	public static void main(String[] args) throws Exception {
		
		
		MemoryMapping[] values = Core.MemoryMapping.values();
		
		for(MemoryMapping mem : values) {
			
			System.out.println(String.format("case %s :  //%s /\n   break;", mem.getName().hashCode(),mem.getName()));
			
		}
		
		
	}

}
