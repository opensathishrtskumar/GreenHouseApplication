package org.lemma.ems.base.dao.dto;

import static org.lemma.ems.util.EMSUtility.getPersistRegisters;
import static org.lemma.ems.util.EMSUtility.getRegisterCount;
import static org.lemma.ems.util.EMSUtility.getRegisterReference;

import java.util.TreeMap;

import org.lemma.ems.util.EMSUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.procimg.InputRegister;

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
	private TreeMap<Long, String> memoryMappings;

	private boolean paramsCalculated = false;

	@Override
	public void calculatePollingParams() {

		// Calculate params once
		if (!paramsCalculated) {

			try {

				setReference((int) getRegisterReference(memoryMappings));
				// Total number of registers to be read from Reference register
				//setCount(getRegisterCount(memoryMappings));
				
				Integer[] registerList = getPersistRegisters(memoryMappings.keySet());
				//setRequiredRegisters(EMSUtility.convertWrapper2Int(registerList));

			} catch (Exception e) {
				logger.error(" Failed to calculatePollingParams : {}", e);
			}

			paramsCalculated = true;
		}

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

	public void setReference(int reference) {
		this.reference = reference;
	}

}
