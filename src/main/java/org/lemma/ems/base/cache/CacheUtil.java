package org.lemma.ems.base.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish  Kumar
 *
 */
@Component
public final class CacheUtil {

	@Autowired
	private CacheManager cacheManager;

	public Cache getCache(Caches cache) {
		return cacheManager.getCache(cache.getName());
	}
	
	/**
	 * {@link Caches.EMSCACHE} is the default instance
	 * 
	 * @param key
	 * @param value
	 * 
	 */
	public void putCacheEntry(String key, Object value) {
		getCache(Caches.EMSCACHE).put(key, value);
	}
	
	/**
	 * @param key
	 * @param className
	 * @return
	 */
	public <T> T getCacheEntry(String key, Class<T> className) {
		return getCache(Caches.EMSCACHE).get(key, className);
	}
	
	/**
	 * @param cache
	 * @param key
	 * @param value
	 */
	public void putCacheEntry(Caches cache, String key, Object value) {
		getCache(cache).put(key, value);
	}

	/**
	 * @param cache
	 * @param key
	 * @param className
	 * @return
	 */
	public <T> T getCacheEntry(Caches cache, String key, Class<T> className) {
		return getCache(cache).get(key, className);
	}
	
	
	
	/**
	 * @param cache
	 * @param key
	 * @param className
	 * @return
	 */
	public Object getCacheEntry(Caches cache, String key) {
		return getCache(cache).get(key);
	}
}
