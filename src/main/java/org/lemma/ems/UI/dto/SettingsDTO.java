package org.lemma.ems.UI.dto;

/**
 * @author RTS Sathish Kumar
 *
 */
public class SettingsDTO {

	private long id;
	private String groupName;
	private String configName;
	private String configValue;
	private long createdTimeStamp;
	private long modifiedTimeStamp;
	private String hashKey;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public long getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(long createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public long getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(long modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	@Override
	public String toString() {
		return "SettingsDTO [groupName=" + groupName + ", configName=" + configName + ", configValue=" + configValue
				+ ", createdTimeStamp=" + createdTimeStamp + ", modifiedTimeStamp=" + modifiedTimeStamp + "]";
	}

}
