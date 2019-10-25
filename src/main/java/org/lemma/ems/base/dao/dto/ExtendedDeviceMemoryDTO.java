package org.lemma.ems.base.dao.dto;

import static org.lemma.ems.base.core.util.MemoryMappingParser.loadMemoryMappingDetails;
import static org.lemma.ems.util.EMSUtility.getPersistRegisters;
import static org.lemma.ems.util.EMSUtility.getRegisterCount;
import static org.lemma.ems.util.EMSUtility.getRegisterReference;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sathish Kumar
 *
 */
public class ExtendedDeviceMemoryDTO extends DeviceMemoryDTO {

	private static final Logger logger = LoggerFactory.getLogger(ExtendedDeviceMemoryDTO.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -3756362028454603167L;
	private int count;
	private int reference;
	private int[] registersRequired;
	private boolean status;
	private Map<Long, String> memoryMappings;

	private boolean paramsCalculated = false;

	@Override
	public void calculatePollingParams() {

		// Calculate params only once
		synchronized (this) {
			if (!paramsCalculated) {

				try {
					// Parse mapping string to SortedMap to find other parameters
					memoryMappings = loadMemoryMappingDetails(getMemoryMapping());
					// Get the Starting register address from where to begin read
					reference = (int) getRegisterReference(getMemoryMappings());
					// Total number of registers to be read from Reference register
					count = getRegisterCount(getMemoryMappings());
					// All the registers required
					registersRequired = getPersistRegisters(getMemoryMappings().keySet());

					paramsCalculated = true;
				} catch (Exception e) {
					logger.error(" Failed to calculatePollingParams : {}", e);
				}
			}
		}
	}

	public Map<Long, String> getMemoryMappings() {
		return memoryMappings;
	}

	public void enableParamRecalculation() {
		paramsCalculated = false;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int[] getRegistersRequired() {
		return registersRequired;
	}

	public boolean isParamsCalculated() {
		return paramsCalculated;
	}

	public int getCount() {
		return count;
	}

	public int getReference() {
		return reference;
	}
}
