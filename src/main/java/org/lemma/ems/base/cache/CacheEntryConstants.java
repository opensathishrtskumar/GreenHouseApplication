package org.lemma.ems.base.cache;

/**
 * @author RTS Sathish Kumar
 *
 */
public class CacheEntryConstants {

	/**
	 * @author RTS Sathish Kumar
	 *
	 */
	public enum DeviceEntryConstants {
		// For DEVICECACHE
		ACTIVE_DEVICES("ACTIVE_DEVICES"),
		GROUPED_ACTIVE_DEVICES("GROUPED_ACTIVE_DEVICES");

		String name;

		private DeviceEntryConstants(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * @author RTS Sathish Kumar
	 *
	 */
	public enum EmsEntryConstants {
		SETTINGS("SETTINGS");

		String name;

		private EmsEntryConstants(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
