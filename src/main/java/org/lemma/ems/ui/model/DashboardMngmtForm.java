
package org.lemma.ems.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DashboardMngmtForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8737934591484167924L;

	private List<Long> dashboardDevices = new ArrayList<>();

	public List<Long> getDashboardDevices() {
		return dashboardDevices;
	}

	public void setDashboardDevices(List<Long> dashboardDevices) {
		this.dashboardDevices = dashboardDevices;
	}

}