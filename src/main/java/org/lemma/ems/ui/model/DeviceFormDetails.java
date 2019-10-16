package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lemma.ems.base.core.constants.Core;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DeviceFormDetails extends Core implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4966391653298875382L;
	private List<Integer> baudRateList = new ArrayList<>();
	private List<Integer> wordlengthList = new ArrayList<>();
	private List<Integer> stopbitList = new ArrayList<>();

	private List<String> parityList = Arrays.asList(PARITIES);
	private List<String> registerMappingList = Arrays.asList(REG_MAPPINGS);
	private List<String> encodingList = Arrays.asList(ENCODINGS);
	private List<String> readMethodList = Arrays.asList(READ_METHODS);

	/**
	 * 
	 */
	public DeviceFormDetails() {
		addElements(baudRateList, BAUDRATES);
		addElements(wordlengthList, WORDLENGTHS);
		addElements(stopbitList, STOPBITS);
	}

	/**
	 * @param list
	 * @param elements
	 */
	private void addElements(List<Integer> list, int[] elements) {
		for (int element : elements) {
			list.add(element);
		}
	}

	public List<Integer> getBaudRateList() {
		return baudRateList;
	}

	public void setBaudRateList(List<Integer> baudRateList) {
		this.baudRateList = baudRateList;
	}

	public List<Integer> getWordlengthList() {
		return wordlengthList;
	}

	public void setWordlengthList(List<Integer> wordlengthList) {
		this.wordlengthList = wordlengthList;
	}

	public List<Integer> getStopbitList() {
		return stopbitList;
	}

	public void setStopbitList(List<Integer> stopbitList) {
		this.stopbitList = stopbitList;
	}

	public List<String> getParityList() {
		return parityList;
	}

	public void setParityList(List<String> parityList) {
		this.parityList = parityList;
	}

	public List<String> getRegisterMappingList() {
		return registerMappingList;
	}

	public void setRegisterMappingList(List<String> registerMappingList) {
		this.registerMappingList = registerMappingList;
	}

	public List<String> getEncodingList() {
		return encodingList;
	}

	public void setEncodingList(List<String> encodingList) {
		this.encodingList = encodingList;
	}

	public List<String> getReadMethodList() {
		return readMethodList;
	}

	public void setReadMethodList(List<String> readMethodList) {
		this.readMethodList = readMethodList;
	}
}
