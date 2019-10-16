package org.lemma.ems.base.core.constants;

import java.util.Arrays;
import java.util.List;

import com.ghgande.j2mod.modbus.Modbus;

/**
 * @author RTS Sathish Kumar
 *
 */
public class Core {

	/**
	 * Supported Baudrates
	 */
	public static final int[] BAUDRATES = { 9600, 14400, 19200, 38400, 57600, 115200, 128000, 256000 };

	public static final int[] WORDLENGTHS = { 7, 8 };

	public static final String[] PARITIES = { "none", "odd", "even", "mark", "space" };

	public static final int[] STOPBITS = { 1, 2 };

	/**
	 * Device Register value order, based on which needs to be interpreted
	 */
	public static final String[] REG_MAPPINGS = { "MSRF", "LSRF" };

	public static final String[] ENCODINGS = { Modbus.SERIAL_ENCODING_ASCII, Modbus.SERIAL_ENCODING_RTU };

	public static final String[] READ_METHODS = { "3", "4" };

	public static final String[] POINTYPES = { "01 - COIL STATUS", "02 - INPUT STATUS", "03 - HOLDING REGISTERS",
			"04 - " + "INPUT REGISTERS" };
}
