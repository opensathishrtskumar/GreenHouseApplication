package org.lemma.ems.util;

import static org.lemma.ems.constants.MessageConstants.REPORT_KEY_SEPARATOR;
import static org.lemma.ems.constants.MessageConstants.REPORT_RECORD_SEPARATOR;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.util.OrderedProperties;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.ExtendedSerialParameter;
import org.lemma.ems.base.dao.dto.SplitJoinDTO;
import org.lemma.ems.constants.EmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author RTS Sathish  Kumar
 *
 */
public abstract class EMSUtility {

	private static final Logger logger = LoggerFactory.getLogger(EMSUtility.class);
	
	public static final String hh_mma = "hh:mma";
	public static final String DASHBOARD_FMT = "dd-MMM,yyyy";
	public static final String DASHBOARD_POLLED_FMT = "dd-MMM,yy hh:mm a";
	public static final String REPORTNAME_FORMAT = "ddMMyyHHmm";

	public static final String SUMMARY_FMT1 = "dd-MM-yy : HH:mm";
	public static final String SUMMARY_FMT = "HH:mm";
	public static final String DD_MM_YY = "dd/MMM/yy";

	public static final String DD_MM_YYYY_HH_MM_S = "dd/MM/yyyy HH:mm:s";
	public static final String EXCEL_REPORTNAME_FORMAT = "ddMMMyyyyHHmm";

	public static Object[] convertObjectArray(int... arg) {
		Object[] response = new Object[arg.length];
		for (int i = 0; i < arg.length; i++) {
			response[i] = String.valueOf(arg[i]);
		}
		return response;
	}

	public static Object[] convertObjectArray(String... arg) {
		Object[] response = new Object[arg.length];
		for (int i = 0; i < arg.length; i++) {
			response[i] = arg[i];
		}
		return response;
	}

	/**
	 * @param mappings
	 * @returns Starting register - Initial required register - 1 is the reference
	 */
	public static long getRegisterReference(Map<Long, String> mappings) {
		long startingRegister = EmsConstants.DEFAULTREGISTER;

		try {
			TreeMap<Long, String> tMap = (TreeMap<Long, String>) mappings;
			NavigableMap<Long, String> nMap = tMap.descendingMap();
			logger.trace("{}", nMap);
			startingRegister = nMap.lastKey() - 1;
		} catch (Exception e) {
			logger.error("{}", e);
			logger.error("Failed to get register reference : {}", e.getLocalizedMessage());
		}

		return startingRegister;
	}

	public static int getRegisterCount(Map<Long, String> mappings) {
		int count = EmsConstants.REGISTERCOUNT;
		try {
			TreeMap<Long, String> tMap = (TreeMap<Long, String>) mappings;
			NavigableMap<Long, String> nMap = tMap.descendingMap();
			long countLong = nMap.firstKey() - nMap.lastKey();
			count = (int) (countLong + 1);
			// Make count is even , so that floating point value calculated

			logger.trace("Register Map : {} - Count : {}", nMap, countLong);
		} catch (Exception e) {
			logger.error("{}", e);
			logger.error("Failed to get register count : {}", e.getLocalizedMessage());
		}

		return count;
	}

	public static String getHHmm() {
		return getFormattedDate("hh:mm a");
	}

	public static String getFormattedDate(String format) {
		SimpleDateFormat formater = new SimpleDateFormat(format);
		return formater.format(new Date(System.currentTimeMillis()));
	}

	public static String getFormattedTime(long timeInMilli, String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date(timeInMilli));
	}

	public static Integer[] getPersistRegisters(Set<Long> set) {
		Integer[] registers = new Integer[set.size()];
		try {
			int index = 0;
			for (Long register : set) {
				registers[index++] = register.intValue();
			}
			Arrays.sort(registers);

		} catch (Exception e) {
			logger.error("{}", e);
		}

		return registers;
	}

	/**
	 * returns memory address and its value in Map structure
	 */
	public static Map<String, String> processRegistersForDashBoard(ExtendedSerialParameter parameters) {

		Map<String, String> finalResponse = new LinkedHashMap<>();

		if (parameters.isSplitJoin()) {
			finalResponse = convertSplitJoinResponse(parameters);
		} else {
			int[] requiredRegisters = parameters.getRequiredRegisters();
			int base = parameters.getReference();
			InputRegister[] registers = parameters.getRegisteres();
			finalResponse = convertRegistersToMap(base, requiredRegisters, registers, parameters.getRegisterMapping());
		}

		logger.trace("Uniqueid {}'s final Response map is {}", parameters.getUniqueId(), finalResponse);

		return finalResponse;
	}

	/**
	 * @param parameters
	 * @return merged Map of SplitJoin Response
	 */
	public static Map<String, String> convertSplitJoinResponse(ExtendedSerialParameter parameters) {
		Map<String, String> finalResponse = new LinkedHashMap<>();
		SplitJoinDTO splitJoinDto = parameters.getSplitJoinDTO();
		String msrfOrLsrf = parameters.getRegisterMapping();

		if (splitJoinDto != null) {
			if (validateSplitJoinDtoValues(splitJoinDto)) {

				List<Integer[]> requiredRegisters = splitJoinDto.getRequiredRegisters();// All the required registers
				List<Integer> reference = splitJoinDto.getReferencce();// Base register
				List<InputRegister[]> registers = splitJoinDto.getRegisteres();// Response registers
				List<Boolean> status = splitJoinDto.getStatus();// Subset execution status

				int index = 0;
				for (int base : reference) {

					if (status.get(index)) {
						Map<String, String> subSetResponse = convertRegistersToMap(base,
								convertWrapper2Int(requiredRegisters.get(index)), registers.get(index), msrfOrLsrf);
						logger.debug(" SplitJoin partial response map {} ", subSetResponse);
						finalResponse.putAll(subSetResponse);
						index++;
					}
				}
			} else {
				logger.info("Split Join DTO values size is different {} ", convertObjectToJSONString(splitJoinDto));
			}
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
	public static Map<String, String> convertRegistersToMap(int base, int[] requiredRegisters,
			InputRegister[] registers, String msrfOrLsrf) {
		Map<String, String> finalResponse = new LinkedHashMap<>();

		for (int reg : requiredRegisters) {
			int registerIndex = reg - (base + 1);
			String value = "00.00";

			try {
				value = getRegisterValue(registerIndex, registers, msrfOrLsrf);
			} catch (Exception e) {
				logger.error("Setting default value for register {}", e);
				value = "00.00";
			}

			finalResponse.put(String.valueOf(reg), value);
		}

		return finalResponse;
	}

	public static String processRequiredRegister(ExtendedSerialParameter parameters) {
		Map<String, String> responseMap = processRegistersForDashBoard(parameters);
		String resonse = convertMapToUnitResponse(responseMap);
		logger.trace("Response for device {} is {}", parameters.getUnitId(), resonse);
		return resonse;
	}

	private static String getRegisterValue(int index, InputRegister[] registers, String registerMapping) {

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
		return convertToFloatWithOrder(bytes, registerMapping);
	}

	public static List<ExtendedSerialParameter> mapDevicesToSerialParams(List<DeviceDetailsDTO> devices) {
		List<ExtendedSerialParameter> paramList = new ArrayList<>();

		for (DeviceDetailsDTO device : devices) {
			paramList.add(mapDeviceToSerialParam(device));
		}

		return paramList;
	}

	public static ExtendedSerialParameter mapDeviceToSerialParam(DeviceDetailsDTO devices) {

		ExtendedSerialParameter parameters = new ExtendedSerialParameter(devices.getPort(), devices.getBaudRate(), 0, 0,
				devices.getWordLength(), devices.getStopbit(), 0, false);

		parameters.setParity(devices.getParity());
		parameters.setRetries(Core.RETRYCOUNT);
		parameters.setTimeout(Core.TIMEOUT);
		parameters.setEncoding(Core.ENCODINGS[1]);
		parameters.setUnitId(devices.getDeviceId());
		parameters.setUniqueId(devices.getUniqueId());
		parameters.setDeviceName(devices.getDeviceName());
		parameters.setRegisterMapping(devices.getRegisterMapping());
		parameters.setPort(devices.getPort());// PortName property should be set
		// to create SerialConnection
		parameters.setMethod(devices.getMethod());

		return parameters;
	}

	/**
	 * Group device by connection param, so that single connection can be reused to
	 * set of devices
	 * 
	 * Keys are created in DeviceDetailsDTO & ExtendedSerialParameter
	 */
	public static Map<String, List<ExtendedSerialParameter>> groupDeviceForPolling(
			List<ExtendedSerialParameter> paramsList) {
		Map<String, List<ExtendedSerialParameter>> groupedDevice = new HashMap<>();

		for (ExtendedSerialParameter device : paramsList) {
			List<ExtendedSerialParameter> group = groupedDevice.get(device.getGroupKey());
			// Create group when doesn't exist
			if (group == null) {
				group = new ArrayList<>();
				groupedDevice.put(device.getGroupKey(), group);
			}
			// Add device in group
			group.add(device);
		}
		return groupedDevice;
	}

	/**
	 * @param bytes
	 *            - read from Modbus slave
	 * @param order
	 *            - the order of which registeres to be processed
	 * @return
	 */
	public static String convertToFloatWithOrder(byte[] bytes, String registeOrder) {
		byte[] byteOrder = null;

		if (registeOrder.equals(Core.REG_MAPPINGS[0])) {
			byteOrder = bytes;
		} else {
			byteOrder = new byte[] { bytes[2], bytes[3], bytes[0], bytes[1] };
		}

		return String.format("%.2f", ModbusUtil.registersToFloat(byteOrder));
	}

	public static Map<String, String> getOrderedProperties(Properties props) {
		Map<String, String> map = new LinkedHashMap<>();

		if (props instanceof OrderedProperties) {
			OrderedProperties orderProp = (OrderedProperties) props;
			Enumeration<Object> list = orderProp.keys();

			while (list.hasMoreElements()) {
				Object key = list.nextElement();
				Object value = props.get(key);
				String val = null;

				if (value != null)
					val = value.toString();

				map.put(key.toString(), val);
			}

		} else {
			map.putAll((Map) props);
		}

		return map;
	}

	public static Map<String, String> getOrderedProperties(ExtendedSerialParameter device) {

		if (device.isSplitJoin()) {
			List<Properties> list = device.getSplitJoinDTO().getProps();
			Map<String, String> map = new LinkedHashMap<>();

			for (Properties props : list) {
				map.putAll(getOrderedProperties(props));
			}

			return map;
		} else {
			return getOrderedProperties(device.getProps());
		}
	}

	public static Object convertJson2Object(String json, Class type) {
		Gson gson = new GsonBuilder().create();
		return (Object) gson.fromJson(json, type);
	}

	public static boolean isNullEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static ModbusRequest getRequest(String method, int reference, int count) {
		if (method.equals(String.valueOf(Modbus.READ_MULTIPLE_REGISTERS))) {
			return new ReadMultipleRegistersRequest(reference, count);
		} else {
			return new ReadInputRegistersRequest(reference, count);
		}
	}

	public static Map<String, String> convertProp2Map(Properties prop) {
		HashMap<String, String> map = new HashMap<>();
		map.putAll((Map) prop);
		return map;
	}

	public static int[] convertWrapper2Int(Integer[] intList) {
		return Arrays.stream(intList).mapToInt(Integer::intValue).toArray();
	}

	public static String convertObjectToJSONString(Object obj) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(obj);
	}

	/**
	 * @param splitJoinDto
	 * @return boolean Checks all the values size of SplitJoinDTO are same
	 */
	private static boolean validateSplitJoinDtoValues(SplitJoinDTO splitJoinDto) {

		return (splitJoinDto.getRequiredRegisters().size() == splitJoinDto.getReferencce().size()
				&& splitJoinDto.getRegisteres().size() == splitJoinDto.getStatus().size());
	}

	/**
	 * @param map
	 * @return String
	 * 
	 *         converts Map to UnitResponse String
	 */
	public static String convertMapToUnitResponse(Map<String, String> map) {
		StringBuilder builder = new StringBuilder();

		for (Entry<String, String> e : map.entrySet()) {
			builder.append(String.valueOf(e.getKey()));
			builder.append(REPORT_KEY_SEPARATOR);
			builder.append(String.valueOf(e.getValue()));
			builder.append(REPORT_RECORD_SEPARATOR);
		}

		return builder.toString();
	}

	/**
	 * @param list
	 * @return
	 */
	public static boolean splitJoinStatus(List<Boolean> list) {
		boolean status = true;

		for (boolean subSet : list) {
			status = status & subSet;
		}

		return status;
	}

	/**
	 * @param count
	 * @param source
	 * @return List which contains sublist of 'count' items
	 * 
	 *         Splits given source list into count number of sublist
	 */
	public static List<ArrayList<DeviceDetailsDTO>> split(int count, List<DeviceDetailsDTO> source) {
		List<ArrayList<DeviceDetailsDTO>> list = new ArrayList<>();

		if (source != null && count > 0) {

			ArrayList<DeviceDetailsDTO> sublist = null;
			for (int index = 0; index < source.size(); index++) {
				if (index % count == 0) {
					sublist = new ArrayList<>();
					list.add(sublist);
				}
				sublist.add(source.get(index));
			}

		}

		return list;
	}

	public static BigDecimal findSecondSmallest(BigDecimal[] a) {
		BigDecimal smallest = a[0];
		BigDecimal secondSmallest = a[0];

		for (int i = 0; i < a.length; i++) {
			if (a[i].compareTo(smallest) == 0) {
				secondSmallest = smallest;
			} else if (a[i].compareTo(smallest) < 0) {
				secondSmallest = smallest;
				smallest = a[i];
			} else if (a[i].compareTo(secondSmallest) < 0) {
				secondSmallest = a[i];
			}
		}

		return secondSmallest;
	}

	/**
	 * @param props
	 * @returns Interchanged key and values
	 */
	public static Properties interChangeKeyValue(Properties props) {

		Properties interchangedProp = new Properties();

		for (Entry<Object, Object> entry : props.entrySet()) {
			interchangedProp.put(entry.getValue(), entry.getKey());
		}

		return interchangedProp;
	}

	public static Pattern getValueByAddressPattern(String memory) {
		return Pattern.compile(memory + "=[\\d.]+");
	}

	public static String getValueByAddress(String values, String memory, Pattern pattern) {

		if (pattern == null)
			pattern = getValueByAddressPattern(memory);

		Matcher matcher = pattern.matcher(values);

		while (matcher.find()) {
			return matcher.group(0).split("=")[1];
		}

		return "0.0";
	}

	public static String getLineSeparator() {
		return System.lineSeparator();
	}

	public static String splitName(String paramName) {
		if (paramName == null || paramName.isEmpty()) {
			return paramName;
		} else {
			String[] params = paramName.split(" ");
			return params.length > 1 ? params[1] : params[0];
		}
	}

	public static long parseDateTime(String dateTime, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(dateTime).getTime();
	}
}
