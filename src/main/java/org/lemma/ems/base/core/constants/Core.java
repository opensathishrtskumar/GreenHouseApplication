package org.lemma.ems.base.core.constants;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.ModbusUtil;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class Core {
	private static final Logger logger = LoggerFactory.getLogger(Core.class);

	private static Pattern pattern = Pattern.compile("(COM)([0-9]){1,}");
	
	
	/**
	 * Guard for Serial Port connection
	 */
	public static final Object SERIAL_MUTEX = new Object();

	/**
	 * valid memory mapping configuration values, used during devicemanagement
	 * 
	 * add value repsresentation like Hz , Voltage and so on against all
	 * mapping
	 */
	public static enum MemoryMapping {
		VOLTAGE_BN("VOLTAGE_BN", "Voltage BN"), 
		VOLTAGE_BR("VOLTAGE_BR", "Voltage BR"), 
		VOLTAGE_RN("VOLTAGE_RN", "Voltage RN"), 
		VOLTAGE_RY("VOLTAGE_RY", "Voltage RY"), 
		VOLTAGE_YB("VOLTAGE_YB", "Voltage YB"), 
		VOLTAGE_YN("VOLTAGE_YN", "Voltage YN"), 
		VOLTAGE_AVG_LL("VOLTAGE_AVG_LL", "Voltage Average LL"), 
		VOLTAGE_AVG_LN("VOLTAGE_AVG_LN", "Voltage Average LN"), 
		R_CURRENT("R_CURRENT", "R Current"),
		Y_CURRENT("Y_CURRENT", "Y Current"), 
		B_CURRENT("B_CURRENT", "B Current"), 
		CURRENT_AVG("CURRENT_AVG", "Current Average"), 
		FREQUENCY("FREQUENCY", "Frequency"), 
		POWER_FACTOR("POWER_FACTOR", "Power Factor"),
		W1("W1", "W1"),
		W2("W2","W2"),
		W3("W3", "W3"), 
		WH("WH", "WH"), 
		W_AVG("W_AVG", "W Average"), 
		VA1("VA1", "VA1"), 
		VA2("VA2", "VA2"), 
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

	/**
	 * Constant gap between every request per connection, i.e COM3 & COM4 will have
	 * different connection altogether
	 */
	public static final int GAP_BETWEEN_REQUEST = 150;

	public static final int[] TIMEOUTS = { 500, 1000, 1500, 2000, 2500 };
	/**
	 * Connetion timeout value
	 */
	public static final int TIMEOUT = TIMEOUTS[1];

	// Keep always zero, otherwise request invalid thread will block
	public static final int RETRYCOUNT = 0;
	
	/**
	 * 
	 */
	public static final long DEFAULTREGISTER = 3900;

	/**
	 * 
	 */
	public static final int REGISTERCOUNT = 10;
	
	/**
	 * 
	 */
	public static final String DEFAULT_REG_VALUE = "00.00";
	

	/**
	 * @returns Available Serial ports as array
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

	/**
	 * @param method
	 * @param reference
	 * @param count
	 * @return
	 */
	protected ModbusRequest createModbusRequest(String method, int reference, int count, int unitID) {
		ModbusRequest request = null;
		if (method.equals(String.valueOf(Modbus.READ_MULTIPLE_REGISTERS))) {
			request = new ReadMultipleRegistersRequest(reference, count);
		} else {
			request = new ReadInputRegistersRequest(reference, count);
		}

		request.setUnitID(unitID);
		return request;
	}

	
	/**
	 * @param bytes
	 *            - read from Modbus slave
	 * @param order
	 *            - the order of which registeres to be processed
	 * @return
	 */
	public static float convertToFloatWithOrder(byte[] bytes, String registeOrder) {
		byte[] byteOrder = null;

		if (registeOrder.equals(Core.REG_MAPPINGS[0])) {
			byteOrder = bytes;
		} else {
			byteOrder = new byte[] { bytes[2], bytes[3], bytes[0], bytes[1] };
		}
		
		return ModbusUtil.registersToFloat(byteOrder);
	}
	
	public static float getRegisterValue(int index, InputRegister[] registers, String registerValueOrder) {

		byte[] bytes = new byte[] { 0, 0, 0, 0 };
		if (registers != null && index < registers.length) {
			byte[] registerBytes = registers[index].toBytes();

			bytes[0] = registerBytes[0];
			bytes[1] = registerBytes[1];

			if (index + 1 < registers.length) {
				registerBytes = registers[index + 1].toBytes();
				bytes[2] = registerBytes[0];
				bytes[3] = registerBytes[1];
			}
		}
		// register ordering MSRF/LSRF
		return convertToFloatWithOrder(bytes, registerValueOrder);
	}
	
	
	/**
	 * @param base
	 * @param requiredRegisters
	 * @param registers
	 * @param msrfOrLsrf
	 * @return Converts InputResgister[] to Map based on base register and MSRF/LSRF
	 */
	public static Map<String, String> convertRegistersToMap(int base, int[] requiredRegisters,
			InputRegister[] registers, String msrfOrLsrf) {
		Map<String, String> finalResponse = new LinkedHashMap<>();

		for (int reg : requiredRegisters) {
			int registerIndex = reg - (base + 1);
			String value = DEFAULT_REG_VALUE;

			try {
				value = String.valueOf(getRegisterValue(registerIndex, registers, msrfOrLsrf));
			} catch (Exception e) {
				logger.error("Setting default value for register {}", e);
				value = DEFAULT_REG_VALUE;
			}

			finalResponse.put(String.valueOf(reg), value);
		}

		return finalResponse;
	}

	
	/**
	 * @param base
	 * @param requiredRegisters
	 * @param registers
	 * @param msrfOrLsrf
	 * @return Converts InputResgister[] to Map based on base register and MSRF/LSRF
	 */
	public static void convertRegistersToMap(ExtendedDeviceMemoryDTO deviceMemory,
			InputRegister[] registers, String msrfOrLsrf, PollingDetailsDTO dto) {
		
		int[] registersRequired = deviceMemory.getRegistersRequired();
		int base = deviceMemory.getReference();
		Map<Long, String> memoryMappings = deviceMemory.getMemoryMappings();
		
		for (int reg : registersRequired) {
			int registerIndex = reg - (base + 1);

			float value = 00.00f;
			
			try {
				value = getRegisterValue(registerIndex, registers, msrfOrLsrf);
			} catch (Exception e) {
				logger.error("Setting default value for register {}", e);
			}
			
			//Get register Name and set value accordingly
			String memory = memoryMappings.get((long)reg);
			
			setReadingValue(memory.hashCode(), dto, value);
		}
	}
	
	
	/**
	 * @param type
	 * @param dto
	 * @param value
	 */
	private static void setReadingValue(int type, PollingDetailsDTO dto, float value) {

		// When there is change in {@Core.MemoryMapping}, change corresponding hashCode
		// value in cases
		switch (type) {
		case 1998804205: // VOLTAGE_BN /
			dto.setVoltage_bn(value);
			break;
		case 1998804209: // VOLTAGE_BR /
			dto.setVoltage_br(value);
			break;
		case 1998804701: // VOLTAGE_RN /
			dto.setVoltage_rn(value);
			break;
		case 1998804712: // VOLTAGE_RY /
			dto.setVoltage_ry(value);
			break;
		case 1998804906: // VOLTAGE_YB /
			dto.setVoltage_yb(value);
			break;
		case 1998804918: // VOLTAGE_YN /
			dto.setVoltage_yn(value);
			break;
		case -649941426: // VOLTAGE_AVG_LL /
			dto.setVoltage_avg_ll(value);
			break;
		case -649941424: // VOLTAGE_AVG_LN /
			dto.setVoltage_avg_ln(value);
			break;
		case 2020769388: // R_CURRENT /
			dto.setR_current(value);
			break;
		case -2041477261: // Y_CURRENT /
			dto.setY_current(value);
			break;
		case 875269724: // B_CURRENT /
			dto.setB_current(value);
			break;
		case -1539497268: // CURRENT_AVG /
			dto.setCurrent_avg(value);
			break;
		case -1578396356: // FREQUENCY /
			dto.setFrequency(value);
			break;
		case -1204453911: // POWER_FACTOR /
			dto.setPower_factor(value);
			break;
		case 2746: // W1 /
			dto.setW1(value);
			break;
		case 2747: // W2 /
			dto.setW2(value);
			break;
		case 2748: // W3 /
			dto.setW3(value);
			break;
		case 2769: // WH /
			dto.setWh(value);
			break;
		case 83241674: // W_AVG /
			dto.setW_avg(value);
			break;
		case 84710: // VA1 /
			dto.setVa1(value);
			break;
		case 84711: // VA2 /
			dto.setVa2(value);
			break;
		case 84712: // VA3 /
			dto.setVa3(value);
			break;
		case 84733: // VAH /
			dto.setVah(value);
			break;
		case -1769936098: // VA_AVG /
			dto.setVa_avg(value);
			break;
		case 68835: // EOM /
			break;
		}

	}
	
	/**
	 * @param response
	 * @returns <tt>InputRegister</tt> obtained from ModbusResposne
	 */
	protected InputRegister[] getResponseRegisters(ModbusResponse response) {
		if (response instanceof ReadMultipleRegistersResponse) {
			return ((ReadMultipleRegistersResponse) response).getRegisters();
		} else {
			return ((ReadInputRegistersResponse) response).getRegisters();
		}
	}
	
	
	/**
	 * @param parameters
	 * @return
	 * @throws IOException 
	 */
	public static SerialConnection getSerialConnection(ExtendedSerialParameter parameters) throws IOException {
		SerialConnection connection = new SerialConnection(parameters);
		connection.open();
		return connection;
	}
	
}
