package org.lemma.ems.base.dao;

import static org.lemma.ems.base.cache.CacheConstants.ACTIVE_DEVICES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.lemma.ems.UI.dto.DeviceGroupDTO;
import org.lemma.ems.UI.dto.DeviceGroupMappingDTO;
import org.lemma.ems.UI.dto.SettingsDTO;
import org.lemma.ems.base.cache.CacheUtil;
import org.lemma.ems.base.dao.constants.QueryConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn({ "deviceGroupMappingDAO" })
public class DeviceGroupMappingDAO {

	private static final Logger logger = LoggerFactory.getLogger(DeviceGroupMappingDAO.class);

	@Autowired
	private CacheUtil cacheUtil;

	@Autowired
	private PollingDetailsDAO pollingDao;

	private List<DeviceGroupMappingDTO> deviceGroupMapping;
	private Map<String, String> settings;

	@PostConstruct
	public void init() {
		loadDeviceGroupMapping();
		loadSettings();
	}

	private void loadDeviceGroupMapping() {
		logger.debug("loading active devices...");
//-->		this.deviceGroupMapping = pollingDao.fetchAllDeviceGroupMapping(QueryConstants.SELECT_ENABLED_ENDEVICES, new Object[] {});
		logger.debug("loaded active devices...");
	}

	private Map<String, String> loadSettings() {
		logger.debug("loading settings....");
		List<SettingsDTO> settingsList = pollingDao.fetchSettings();
		this.settings = new HashMap<>();

		for (SettingsDTO setting : settingsList) {
			this.settings.put(setting.getConfigName(), setting.getConfigValue());
		}

		logger.debug("settings loaded...");
		return this.settings;
	}

	public List<DeviceGroupMappingDTO> getDeviceGroupMapping() {
		return deviceGroupMapping;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public List<DeviceGroupMappingDTO> fetchActiveDeviceGroupMapping() {
		logger.debug("loading active devices...");
		List<DeviceGroupMappingDTO> activeDevices = cacheUtil.getCacheEntry(ACTIVE_DEVICES, List.class);

		if (activeDevices == null) {
//-->			activeDevices = pollingDao.fetchAllDeviceGroupMapping(QueryConstants.SELECT_ENABLED_ENDEVICES, new Object[] {});
			cacheUtil.putCacheEntry(ACTIVE_DEVICES, activeDevices);
		}

		logger.debug("loaded active devices...");
		return deviceGroupMapping;
	}

}