package org.lemma.ems.constants;

/**
 * @author RTS Sathish Kumar
 *
 */
public final class EmsConstants {

	private EmsConstants() {
		// NOOP
	}

	public static final int[] TIMEOUT = { 500, 1000, 1500, 2000, 2500 };

	// Keep always zero, otherwise request invalid thread will block
	public static final int RETRYCOUNT = 0;

	public static final long DEFAULTREGISTER = 3900;

	public static final int REGISTERCOUNT = 10;

	public static final int GAP_BETWEEN_REQUEST = 150;

	public static final String SPLIT_JOIN = "9999=split";
}
