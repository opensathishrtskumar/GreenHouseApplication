package org.lemma.infra.config.listener;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class MainMaster {


	public static void main(String[] args) throws Exception {

		CircularFifoQueue<String> queue = new CircularFifoQueue<>(5);
		
		for(int i = 0;i<10;i++) {
			queue.add("Elelemet " + i);
			System.out.println(queue);
		}
		
	}

}
