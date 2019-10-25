package org.lemma.ems.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.stream.Collectors;

import org.lemma.ems.base.core.EMSDeviceResponseHolder;
import org.lemma.ems.base.core.ExtendedSerialParameter;
import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.util.OrderedProperties;
import org.lemma.ems.base.dao.dto.DeviceDetailsDTO;
import org.lemma.ems.base.dao.dto.ExtendedDeviceMemoryDTO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class EMSUtility {

	private static final Logger logger = LoggerFactory.getLogger(EMSUtility.class);

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
		long startingRegister = Core.DEFAULTREGISTER;

		try {
			TreeMap<Long, String> tMap = (TreeMap<Long, String>) mappings;
			NavigableMap<Long, String> nMap = tMap.descendingMap();
			startingRegister = nMap.lastKey() - 1;
		} catch (Exception e) {
			logger.error("{}", e);
			logger.error("Failed to get register reference : {}", e.getLocalizedMessage());
		}

		return startingRegister;
	}

	public static int getRegisterCount(Map<Long, String> mappings) {
		int count = Core.REGISTERCOUNT;
		try {
			TreeMap<Long, String> tMap = (TreeMap<Long, String>) mappings;
			NavigableMap<Long, String> nMap = tMap.descendingMap();
			long countLong = nMap.firstKey() - nMap.lastKey();
			// Make count is even , so that floating point value calculated
			count = (int) (countLong + 1);
			logger.trace("Register Map : {} - Count : {}", nMap, countLong);
		} catch (Exception e) {
			logger.error("{}", e);
			logger.error("Failed to get register count : {}", e.getLocalizedMessage());
		}

		return count;
	}

	/**
	 * @param set
	 * @return
	 */
	public static int[] getPersistRegisters(Set<Long> set) {
		int[] registers = new int[set.size()];
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
	 * Group device by connection param, so that single connection can be reused to
	 * set of devices
	 * 
	 * Keys are created in DeviceDetailsDTO & ExtendedSerialParameter
	 */
	public static Map<String, List<ExtendedSerialParameter>> groupDevicesForPolling(
			List<ExtendedSerialParameter> paramsList) {
		return paramsList.stream().collect(Collectors.groupingBy(ExtendedSerialParameter::getGroupByKey));
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

	@SuppressWarnings("unchecked")
	public static Object convertJson2Object(String json, Class type) {
		Gson gson = new GsonBuilder().create();
		return (Object) gson.fromJson(json, type);
	}

	public static boolean isNullEmpty(String value) {
		return value == null || value.trim().isEmpty();
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
	
	public static PollingDetailsDTO populatePollingValuesToDto(EMSDeviceResponseHolder dtopoll) {
		PollingDetailsDTO dto = new PollingDetailsDTO();
		dto.setPolledOn(dtopoll.getTimeOfPoll());
		dto.setDeviceUniqueId(dtopoll.getDevice().getUniqueId());

		List<ExtendedDeviceMemoryDTO> deviceMemoryList = dtopoll.getDevice().getDeviceMemoryList();

		int index = 0;
		for (ExtendedDeviceMemoryDTO deviceMemory : deviceMemoryList) {

			InputRegister[] inputRegisters = dtopoll.getResponseRegisters().get(index);
			String registerValueOrder = dtopoll.getDevice().getRegisterMapping();
			// Extracts value from InputRegister[] and sets values to PollingDetailsDTO
			Core.convertRegistersToMap(deviceMemory, inputRegisters, registerValueOrder, dto);

			index += 1;
		}

		return dto;
	}
}
