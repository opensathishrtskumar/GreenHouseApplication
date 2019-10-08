package org.lemma.ems.base.cache;

public enum Caches {

	ETERNAL("emscache");
	
	String name;

	private Caches(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
