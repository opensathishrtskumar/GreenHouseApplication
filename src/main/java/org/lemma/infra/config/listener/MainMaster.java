package org.lemma.infra.config.listener;

import org.lemma.ems.util.BitUtil;

public class MainMaster {

	int[] uniqueid;

	public static void main(String[] args) throws Exception {
		long number = 0;
		
		
		number  =  BitUtil.setBit(number, 3);
		number  =  BitUtil.setBit(number, 12);
		System.out.println(number);
		number  =  BitUtil.unsetBit(number, 3);
		System.out.println(BitUtil.checkBit(number, 3));
		
		
		
	}

}
