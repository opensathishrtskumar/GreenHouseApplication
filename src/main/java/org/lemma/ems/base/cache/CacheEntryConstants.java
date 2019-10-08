package org.lemma.ems.base.cache;

public enum CacheEntryConstants {

	ACTIVE_DEVICES("ACTIVE_DEVICES"), SETTINGS("SETTINGS");

	String name;

	private CacheEntryConstants(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
