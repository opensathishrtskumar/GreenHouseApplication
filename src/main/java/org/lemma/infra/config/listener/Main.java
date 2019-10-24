package org.lemma.infra.config.listener;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
		
		float d = 0;
		
		for(int i=0;i<=31;i++) {
			d = d + (1 << i);
			System.out.println(d);
		}

	}

}
