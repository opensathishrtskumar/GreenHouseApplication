package org.lemma.ems.util;

import static org.lemma.ems.constants.LimitConstants.DASHBOARD_DEVICES_COUNT;
import static org.lemma.ems.constants.LimitConstants.DASHBOARD_REFRESH_FREQUENCY;
import static org.lemma.ems.constants.LimitConstants.DEFAULT_COMPANY_NAME;
import static org.lemma.ems.constants.LimitConstants.DEFAULT_COMPORT;
import static org.lemma.ems.constants.LimitConstants.DEFAULT_NUMBER_OF_DEVICES;
import static org.lemma.ems.constants.MessageConstants.COMPANYNAME_KEY;
import static org.lemma.ems.constants.MessageConstants.DASHBOARD_DEVICESCOUNT_KEY;
import static org.lemma.ems.constants.MessageConstants.DASHBOARD_DEVICES_KEY;
import static org.lemma.ems.constants.MessageConstants.DASHBOARD_REFRESHFREQUENCY_KEY;
import static org.lemma.ems.constants.MessageConstants.DEFAULTPORT_KEY;
import static org.lemma.ems.constants.MessageConstants.DEVICES_GROUPING_KEY;
import static org.lemma.ems.constants.MessageConstants.EMAIL_DETAILS_KEY;
import static org.lemma.ems.constants.MessageConstants.NUMBER_OF_DEVICES_KEY;
import static org.lemma.ems.constants.MessageConstants.PASSWORD_KEY;
import static org.lemma.ems.constants.MessageConstants.USERNAME_KEY;

import java.util.Properties;
import java.util.Random;

import org.lemma.ems.base.dao.dto.EmailDTO;
import org.lemma.ems.constants.LimitConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RTS Sathish Kumar
 *
 */
public abstract class Helper {
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(Helper.class);
	/**
	 * 
	 */
	private static final int PWD_LENGTH = 9;
	/**
	 * 
	 */
	private static final String[] SEEDS = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
			"!", "@", "#", "$", "*", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * @return
	 */
	public static String generateRandomPassword() {
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (int i = 1; i <= PWD_LENGTH; i++) {
			builder.append(SEEDS[random.nextInt(SEEDS.length)]);
		}
		return builder.toString();
	}

	/**
	 * Config properties with default values
	 */
	public static Properties getInitialMainConfig() {
		Properties mainConfig = new Properties();
		mainConfig.put(USERNAME_KEY, "admin");
		mainConfig.put(PASSWORD_KEY, generateRandomPassword());
		mainConfig.put(DASHBOARD_DEVICESCOUNT_KEY, String.valueOf(DASHBOARD_DEVICES_COUNT));
		mainConfig.put(DASHBOARD_REFRESHFREQUENCY_KEY, String.valueOf(DASHBOARD_REFRESH_FREQUENCY));
		mainConfig.put(DASHBOARD_DEVICES_KEY, "");
		mainConfig.put(COMPANYNAME_KEY, DEFAULT_COMPANY_NAME);
		mainConfig.put(DEFAULTPORT_KEY, DEFAULT_COMPORT);
		mainConfig.put(NUMBER_OF_DEVICES_KEY, String.valueOf(DEFAULT_NUMBER_OF_DEVICES));

		// Device grouping
		mainConfig.put(DEVICES_GROUPING_KEY, LimitConstants.DEFAULT_GROUP_VALUE);
		// Email details
		mainConfig.put(EMAIL_DETAILS_KEY, EMSUtility.convertObjectToJSONString(new EmailDTO()));
		// Dont add any more keys here - Confighelper is coded to put default
		// value

		logger.info("Initiatal setup is completed...");
		return mainConfig;
	}

	/**
	 * @param value
	 * @return
	 */
	public static boolean checkNullEmpty(String value) {
		return value == null || value.isEmpty();
	}

	/**
	 * @param hiddeMe
	 * @return
	 */
	public static String hidePassword(String hiddeMe) {
		StringBuilder builder = new StringBuilder(hiddeMe);
		int length = builder.length();
		Random random = new Random();
		for (int i = 0; i < length * 2; i = i + 2) {
			builder.insert(i, SEEDS[random.nextInt(SEEDS.length)]);
		}
		return builder.toString();
	}

	/**
	 * Algorithm selects 'x' records from 'n' size array
	 */
	public static int[] selectOptimalRecords(int listSize, int requiredRecordCount) {
		int n = listSize;
		int x = requiredRecordCount;
		int[] selected = null;

		if (n <= x || n == 0 || x == 0) {
			int loop = n < x ? n : x;
			selected = new int[loop];
			for (int i = 0; i < loop; i++)
				selected[i] = i;
			return selected;
		}

		selected = new int[x];
		int diff = n / x;

		for (int i = 0; i < x; i++)
			selected[(x - i) - 1] = n - (i * diff) - 1;

		return selected;
	}

	public static void main(String[] args) throws Exception {

	}
}
