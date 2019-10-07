package org.lemma.ems.reports.summary.finder;

import java.sql.ResultSet;
import java.util.Map;

import org.lemma.ems.reports.summary.MeterSummary;

public abstract class SummaryFinder {
	
	public abstract void process(ResultSet rs) throws Exception;
	
	public abstract Map<String, MeterSummary> getResponse();
	
}
