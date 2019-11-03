package org.lemma.ems.ui.model;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

/**
 * @author RTS Sathish Kumar
 *
 */
public class UserRolesForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676793238282380110L;

	@NotNull
	private long uniqueId[];
	@NotNull
	private int bitPosition[];

	public UserRolesForm() {
		// NOOP
	}

	public long[] getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(long[] uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int[] getBitPosition() {
		return bitPosition;
	}

	public void setBitPosition(int[] bitPosition) {
		this.bitPosition = bitPosition;
	}

}
