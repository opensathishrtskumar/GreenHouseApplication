package org.lemma.ems.base.core.constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.Modbus;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class Core {
	private static final Logger logger = LoggerFactory.getLogger(Core.class);
	
	private static Pattern pattern = Pattern.compile("(COM)([0-9]){1,}");
	
	/**
	 * valid memory mapping configuration values, used during devicemanagement
	 *
	 */
	public static enum MemoryMapping {
		VOLTAGE_BN("VOLTAGE_BN","Voltage BN"), 
		VOLTAGE_BR("VOLTAGE_BR","Voltage BR"), 
		VOLTAGE_RN("VOLTAGE_RN","Voltage RN"), 
		VOLTAGE_RY("VOLTAGE_RY","Voltage RY"), 
		VOLTAGE_YB("VOLTAGE_YB","Voltage YB"), 
		VOLTAGE_YN("VOLTAGE_YN","Voltage YN"), 
		VOLTAGE_AVG_LL("VOLTAGE_AVG_LL","Voltage Average LL"), 
		VOLTAGE_AVG_LN("VOLTAGE_AVG_LN","Voltage Average LN"), 
		R_CURRENT("R_CURRENT","R Current"), 
		Y_CURRENT("Y_CURRENT","Y Current"), 
		B_CURRENT("B_CURRENT","B Current"), 
		CURRENT_AVG("CURRENT_AVG","Current Average"), 
		FREQUENCY("FREQUENCY","Frequency"), 
		POWER_FACTOR("POWER_FACTOR","Power Factor"), 
		W1("W1","W1"), 
		W2("W2","W2"), 
		W3("W3","W3"), 
		WH("WH","WH"), 
		W_AVG("W_AVG","W Average"), 
		VA1("VA1","VA1"), 
		VA2("VA2","VA2"), 
		VA3("VA3","VA3"), 
		VAH("VAH","VAH"), 
		VA_AVG("VA_AVG","VA Average"),
		EOM("EOM","End of Mapping");

		String name;
		String desc;

		private MemoryMapping(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}

		public String getName() {
			return name;
		}
		
		/**
		 * @return pollingdetails table column name - same as enum but lowercase
		 */
		public String getColumn() {
			return name.toLowerCase();
		}
	}
	
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
	
	public static final String[] MEMORY_MAPPINGS = new String[] {};
	
	/**
	 * return Available Serial ports as array
	 */
	public static String[] getAvailablePort() {
		String[] availablePorts = {};

		try {
			SerialPort[] ports = SerialPort.getCommPorts();

			if (ports != null) {
				availablePorts = new String[ports.length];

				for (int i = 0; i < ports.length; i++) {
					availablePorts[i] = ports[i].getSystemPortName();
				}
			}

		} catch (Exception e) {
			logger.error("Failed to load Serial Ports : {} ", e.getLocalizedMessage());
		}

		return availablePorts;
	}

	/**
	 * @param portName
	 * @return Extract only port name from descriptive name
	 */
	public static String extractPortName(String portName) {
		Matcher matcher = pattern.matcher(portName);
		if (matcher.find())
			portName = matcher.group();
		return portName;
	}
}
