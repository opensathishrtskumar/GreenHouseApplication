package org.lemma.ems.base.cache;

/**
 * @author RTS Sathish  Kumar
 * 
 * Different Cache instance name
 */
public enum Caches {

	EMSCACHE("emscache"), 
	DEVICECACHE("devicecache");

	String name;

	private Caches(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
