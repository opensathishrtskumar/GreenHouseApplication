package org.lemma.ems.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public final class CacheUtil {

	@Autowired
	private CacheManager cacheManager;

	public Cache getCache(Caches cache) {
		return cacheManager.getCache(cache.getName());
	}
	
	/**
	 * {@link Caches.ETERNAL} is the default instance
	 * 
	 * @param key
	 * @param value
	 * 
	 */
	public void putCacheEntry(String key, Object value) {
		getCache(Caches.ETERNAL).put(key, value);
	}
	
	/**
	 * @param key
	 * @param className
	 * @return
	 */
	public <T> T getCacheEntry(String key, Class<T> className) {
		return getCache(Caches.ETERNAL).get(key, className);
	}
	
	public void putCacheEntry(Caches cache, String key, Object value) {
		getCache(cache).put(key, value);
	}

	public <T> T getCacheEntry(Caches cache, String key, Class<T> className) {
		return getCache(cache).get(key, className);
	}

}
