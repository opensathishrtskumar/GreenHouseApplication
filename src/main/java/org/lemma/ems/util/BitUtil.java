package org.lemma.ems.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class BitUtil {

	private static final Logger logger = LoggerFactory.getLogger(BitUtil.class);
	
	/**
	 * @param number
	 * @param position
	 * @return given bit position set in number
	 */
	public static boolean checkBit(long number, int position) {
		return (number & (1 << position)) > 0;
	}
	
	
	/**
	 * @param number
	 * @param position
	 * @return
	 */
	public static long setBit(long number, int position) {
		return (number | (1 << position));
	}
	
	
	/**
	 * @param number
	 * @param position
	 * @return
	 */
	public static long unsetBit(long number, int position) {
		return (number & ~(1 << position));
	}
}
