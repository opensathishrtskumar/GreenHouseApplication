package org.lemma.ems.base.core.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lemma.ems.base.core.constants.Core;
import org.lemma.ems.base.core.constants.Core.MemoryMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RTS Sathish Kumar Parses memory mapping and returns as key=value pair
 */
public class MemoryMappingParser {

	private static final Logger logger = LoggerFactory.getLogger(MemoryMappingParser.class);

	public static final Pattern MEMORY_MAPPING_PAT = Pattern.compile("(.*)(=)(.*)");

	public static final Pattern MARKER_MAPPPING_PAT = Pattern.compile(Core.MemoryMapping.EOM.getName(),
			Pattern.CASE_INSENSITIVE);

	/**
	 * @param memoryMappingReverse
	 * @return
	 */
	public static Map<String, Integer> parseMappingDescAndAddress(String memoryMappingReverse) {
		Map<String, Integer> memoryMappingMap = new LinkedHashMap<>();

		Matcher matcher = MEMORY_MAPPING_PAT.matcher(memoryMappingReverse);
		while (matcher.find()) {
			memoryMappingMap.put(matcher.group(3), Integer.parseInt(matcher.group(1)));
		}

		return memoryMappingMap;
	}

	/**
	 * Parse actual memory mapping to Map<Integer,String>
	 * 
	 * @param memoryMapping
	 * @return
	 */
	public static Map<Integer, String> parseMappingAddressAndDesc(String memoryMapping) {
		Map<Integer, String> memoryMappingMap = new LinkedHashMap<>();

		Matcher matcher = MEMORY_MAPPING_PAT.matcher(memoryMapping);
		while (matcher.find()) {
			memoryMappingMap.put(Integer.parseInt(matcher.group(1)), matcher.group(3));
		}

		return memoryMappingMap;
	}

	/**
	 * Validates all Memory mapping desc against {@link Core.MemoryMapping} desc
	 * Every memory mapping is expected as key=value pair, i.e 4001=R_CURRENT, along
	 * with {@link Core.MemoryMapping.EOM} End of mapping pointer <b>NOTE :<b>
	 * Maximum we have 25 param including EOM
	 * 
	 * @param mappings
	 * @return
	 */
	public static boolean validateAddressAndDesc(Map<Integer, String> mappings) {

		if (mappings == null || mappings.size() == 0)
			return false;

		// Every entry's value should exist against Core.MemoryMapping
		for (Entry<Integer, String> entry : mappings.entrySet()) {
			MemoryMapping valueOf = Core.MemoryMapping.valueOf(entry.getValue());

			if (valueOf == null)
				return false;
		}

		return true;
	}

	/**
	 * @param memoryMappingTxt
	 * @return
	 */
	public static Map<Integer, String> parseAndValidateMappings(String memoryMappingTxt) throws Exception {
		Map<Integer, String> parsedMappingAddressAndDesc = parseMappingAddressAndDesc(memoryMappingTxt);

		boolean validatedAddressAndDesc = validateAddressAndDesc(parsedMappingAddressAndDesc);

		if (!validatedAddressAndDesc)
			throw new Exception("Memory mapping validation failure");

		return parsedMappingAddressAndDesc;
	}
}
