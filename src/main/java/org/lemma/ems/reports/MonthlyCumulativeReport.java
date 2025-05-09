package org.lemma.ems.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.joda.time.LocalDate;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.jxls.util.Util;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.reports.model.MonthlyReportHolder;
import org.lemma.ems.reports.model.MonthlyReportModel;
import org.lemma.ems.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author RTS Sathish Kumar
 *
 */

@Component
public class MonthlyCumulativeReport {
	static Logger logger = LoggerFactory.getLogger(MonthlyCumulativeReport.class);
	private static String template = "Monthly_Templates.xls";
	private static String output = System.getProperty("java.io.tmpdir") + System.getProperty("path.separator")
			+ "MonthlyTemplates_output.xls";

	@Autowired
	PollingDetailsDAO pollingDetailsDAO;

	@Autowired
	DeviceDetailsDAO deviceDetailsDAO;

	public static void main(String[] args) throws IOException {
		logger.info("Running Multiple Sheet demo");
	}

	public void execute() throws IOException {

		MonthlyReportHolder holder = new MonthlyReportHolder();
		holder.setCompanyName("Isuzu Motors India");// FIXME: retrieve from settings
		holder.setTemplateName(template);
		long yesterday = LocalDate.now().minusDays(1).toDate().getTime();
		SimpleDateFormat originalFormat = new SimpleDateFormat("MMM");
		String date = originalFormat.format(yesterday);

		holder.setMonth(date);

		InputStream imageInputStream = MonthlyCumulativeReport.class.getResourceAsStream("Isuzu.png");// FIXME: retrieve from
																										// settings
		byte[] imageBytes = Util.toByteArray(imageInputStream);
		String img = Base64.getEncoder().encodeToString(imageBytes);
		byte[] decode = Base64.getDecoder().decode(img);

		List<MonthlyReportModel> arrayList = populateDeviceList();
		holder.setDeviceList(arrayList);

		try (InputStream is = MonthlyCumulativeReport.class.getResourceAsStream(template)) {

			java.io.File file = new java.io.File(output);

			if (!file.exists())
				file.createNewFile();

			try (OutputStream os = new FileOutputStream(file)) {
				Context context = new Context();
				List<String> collect = holder.getDeviceList().stream().map(MonthlyReportModel::getShortFeederName)
						.collect(Collectors.toList());

				context.putVar("companyName", holder.getCompanyName());
				context.putVar("image", decode);
				context.putVar("deviceList", holder.getDeviceList());
				context.putVar("sheetNames", collect);
				context.putVar("month", holder.getMonth());
				
				JxlsHelper instance = JxlsHelper.getInstance();
				instance.setDeleteTemplateSheet(true);
				instance.setHideTemplateSheet(true);
				instance.setProcessFormulas(true);
				instance.processTemplate(is, os, context);
			}
		}
	}

	private List<MonthlyReportModel> populateDeviceList() {
		List<MonthlyReportModel> arrayList = new ArrayList<>();
		//LocalDate ldate = LocalDate.parse("01-01-2019", new org.joda.time.format.DateTimeFormatter(printer, parser));
		LocalDate endDate = LocalDate.now().minusDays(1);
		 
		long yesterday = LocalDate.now().minusDays(1).toDate().getTime();
		//String month = yesterday;
		LocalDate beginDate = endDate.withDayOfMonth(1);
		long startOfDay = DateUtil.getStartOfDay(beginDate.toDate().getTime());
		// Add one hour too get last next day first record too
		long endOfDay = DateUtil.getEndOfDay(endDate.toDate().getTime());

		List<PollingDetailsDTO> cumulativeRecords = pollingDetailsDAO.fetchMonthlyPolledSummary(
				PollingDetailsDAO.FETCH_MONTHLY_CUMULATIVE_DETAILS,
				 DeviceDetailsDAO.Status.ACTIVE.getStatus(), DeviceDetailsDAO.Type.EMS.getType(),startOfDay, endOfDay );

		Map<Long, List<PollingDetailsDTO>> collect = cumulativeRecords.stream()
				.collect(Collectors.groupingBy(PollingDetailsDTO::getUniqueId));

		for (Entry<Long, List<PollingDetailsDTO>> entry : collect.entrySet()) {
			Long uniqueid = entry.getKey();
			List<PollingDetailsDTO> value = entry.getValue();

			MonthlyReportModel reportModel = new MonthlyReportModel(
					"Feeder " + uniqueid /* TODO : get feeder name from cache */, uniqueid.intValue(),
					DateUtil.getFormattedTime(yesterday, DateUtil.DD_MM_YY));

			Map<String, Object> map = new HashedMap();
			float minValue;
			float maxValue;
			String minConsumeDate;
			String maxConsumeDate;
			minValue = -1;
			maxValue = 0;
			minConsumeDate = "";
			maxConsumeDate = "";
			for (int i = 0; i < value.size() - 1; i++) {
				PollingDetailsDTO currentIndex = value.get(i);
				PollingDetailsDTO nextIndex = value.get(i + 1);

				String key = String.valueOf(Integer.parseInt(currentIndex.getTimeFormat().split("-")[1]));
				float difference = nextIndex.getVoltage_br() - currentIndex.getVoltage_br();
				map.put(key, difference);
				if(minValue ==-1 ) {
		            minValue = difference;
		            minConsumeDate = key;					
				}else if(difference < minValue){
		            minValue = difference;
		            minConsumeDate = key;
		        }
				if(difference > maxValue){
					maxValue = difference;
					maxConsumeDate = key;
				}
			}			
			PollingDetailsDTO begining = value.get(0);
			PollingDetailsDTO end = value.get(value.size() - 1);
			// last record - first record
			reportModel.setTotalConsumption(String.valueOf(end.getVoltage_br() - begining.getVoltage_br()));
			reportModel.setConsumption(map);
			reportModel.setMinConsumptionDate(minConsumeDate);
			reportModel.setMaxConsumptionDate(maxConsumeDate);
			arrayList.add(reportModel);
		}
		return arrayList;
	}
}
