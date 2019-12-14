package org.lemma.ems.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.joda.time.LocalDateTime;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.jxls.util.Util;
import org.lemma.ems.base.dao.DeviceDetailsDAO;
import org.lemma.ems.base.dao.PollingDetailsDAO;
import org.lemma.ems.base.dao.dto.PollingDetailsDTO;
import org.lemma.ems.reports.model.DailyReportHolder;
import org.lemma.ems.reports.model.DailyReportModel;
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

		DailyReportHolder holder = new DailyReportHolder();
		holder.setCompanyName("Isuzu Motors India");// retrieve from settings
		holder.setTemplateName(template);
		InputStream imageInputStream = MonthlyCumulativeReport.class.getResourceAsStream("Isuzu.png");// retrieve from
																										// settings
		byte[] imageBytes = Util.toByteArray(imageInputStream);
		String img = Base64.getEncoder().encodeToString(imageBytes);
		byte[] decode = Base64.getDecoder().decode(img);

		List<DailyReportModel> arrayList = populateDeviceList();
		holder.setDeviceList(arrayList);

		try (InputStream is = MonthlyCumulativeReport.class.getResourceAsStream(template)) {

			java.io.File file = new java.io.File(output);

			if (!file.exists())
				file.createNewFile();

			try (OutputStream os = new FileOutputStream(file)) {
				Context context = new Context();
				List<String> collect = holder.getDeviceList().stream().map(DailyReportModel::getShortFeederName)
						.collect(Collectors.toList());

				context.putVar("companyName", holder.getCompanyName());
				context.putVar("image", decode);
				context.putVar("deviceList", holder.getDeviceList());
				context.putVar("sheetNames", collect);

				JxlsHelper instance = JxlsHelper.getInstance();
				instance.setDeleteTemplateSheet(true);
				instance.setHideTemplateSheet(true);
				instance.setProcessFormulas(true);
				instance.processTemplate(is, os, context);
			}
		}
	}

	private List<DailyReportModel> populateDeviceList() {
		List<DailyReportModel> arrayList = new ArrayList<>();

		long yesterday = LocalDateTime.now().minusDays(1).toDate().getTime();
		long startOfDay = DateUtil.getStartOfDay(yesterday);
		// Add one hour too get last next day first record too
		long endOfDay = DateUtil.getEndOfDay(yesterday) + (60 * 60 * 1000);

		List<PollingDetailsDTO> cumulativeRecords = pollingDetailsDAO.fetchMonthlyPolledSummary(
				PollingDetailsDAO.FETCH_MONTHLY_CUMULATIVE_DETAILS,
				 DeviceDetailsDAO.Status.ACTIVE.getStatus(), DeviceDetailsDAO.Type.EMS.getType(),startOfDay, endOfDay );

		Map<Long, List<PollingDetailsDTO>> collect = cumulativeRecords.stream()
				.collect(Collectors.groupingBy(PollingDetailsDTO::getUniqueId));

		for (Entry<Long, List<PollingDetailsDTO>> entry : collect.entrySet()) {
			Long uniqueid = entry.getKey();
			List<PollingDetailsDTO> value = entry.getValue();

			DailyReportModel reportModel = new DailyReportModel(
					"Feeder " + uniqueid /* TODO : get feeder name from cache */, uniqueid.intValue(),
					DateUtil.getFormattedTime(yesterday, DateUtil.DD_MM_YY));

			Map<String, Object> map = new HashedMap();

			for (int i = 0; i < value.size() - 1; i++) {
				PollingDetailsDTO currentIndex = value.get(i);
				PollingDetailsDTO nextIndex = value.get(i + 1);

				String key = currentIndex.getTimeFormat().split("-")[1];
				float difference = nextIndex.getVoltage_br() - currentIndex.getVoltage_br();

				map.put(key, difference);
			}

			PollingDetailsDTO begining = value.get(0);
			PollingDetailsDTO end = value.get(value.size() - 1);
			// last record - first record
			reportModel.setTotalConsumption(String.valueOf(end.getVoltage_br() - begining.getVoltage_br()));
			reportModel.setConsumption(map);

			arrayList.add(reportModel);
		}

		return arrayList;
	}
}
