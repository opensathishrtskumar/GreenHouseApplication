package org.lemma.ems.base.core.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

/**
 * @author RTS Sathish Kumar
 *
 */
public class OrderedProperties extends Properties {
	private static final long serialVersionUID = 1L;
	private final LinkedHashSet<Object> keyOrder = new LinkedHashSet<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#keys()
	 */
	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(keyOrder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public synchronized Object put(Object key, Object value) {
		keyOrder.add(key);
		return super.put(key, value);
	}
}