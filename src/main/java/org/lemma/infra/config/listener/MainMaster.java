package org.lemma.infra.config.listener;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MainMaster {

	public static class X {

		private String a;
		private String b;

		public X() {

		}

		public X(String a) {
			this.a = a;
		}

		public X(String a, String b) {
			this.a = a;
			this.b = b;
		}

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}

		public String getB() {
			return b;
		}

		public void setB(String b) {
			this.b = b;
		}

		@Override
		public String toString() {
			return "X [a=" + a + ", b=" + b + "]";
		}
	}

	public static void main(String[] args) throws Exception {

		CircularFifoQueue<String> queue = new CircularFifoQueue<>(5);
		
		for(int i = 0;i<10;i++) {
			queue.add("Elelemet " + i);
			System.out.println(queue);
		}
		List<X> list = new ArrayList<>();
		list.add(new X("a"));
		list.add(new X("a","b"));
		list.add(new X("ax","by"));
		list.add(new X("ax","bx"));
		
		/*System.out.println(list);
		
		for(X x : list) {
			System.out.println(x);
		}
		
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}*/
		
		Map<String, List<X>> group = list.stream().collect(Collectors.groupingBy(X::getA));
		
		
		for(Entry<String, List<X>> entry : group.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println("");
			
			for(X x : entry.getValue()) {
				System.out.println("\t"+ entry.getValue());
			}
			
		}
		
		//System.out.println(group);
		
	}

}
