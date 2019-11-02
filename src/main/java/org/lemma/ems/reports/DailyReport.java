package org.lemma.ems.reports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.jxls.util.Util;
import org.lemma.ems.reports.model.DailyReportHolder;
import org.lemma.ems.reports.model.DailyReportModel;
import org.lemma.ems.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author RTS Sathish Kumar
 *
 */
public class DailyReport {
	static Logger logger = LoggerFactory.getLogger(DailyReport.class);

	private static String template = "Templates.xls";
	private static String output = "target/Templates_output.xls";

	public static void main(String[] args) throws IOException {
		logger.info("Running Multiple Sheet demo");
		execute();
	}

	public static void execute() throws IOException {

		DailyReportHolder holder = new DailyReportHolder();
		holder.setCompanyName("Isuzu Motors India");
		holder.setTemplateName(template);
		InputStream imageInputStream = DailyReport.class.getResourceAsStream("Isuzu.png");
		byte[] imageBytes = Util.toByteArray(imageInputStream);
		holder.setEncodedImage(Base64.getEncoder().encodeToString(imageBytes));
		List<DailyReportModel> arrayList = populateDeviceList();
		holder.setDeviceList(arrayList);

		try (InputStream is = DailyReport.class.getResourceAsStream(template)) {
			try (OutputStream os = new FileOutputStream(output)) {
				Context context = new Context();
				List<String> collect = holder.getDeviceList().stream().map(DailyReportModel::getShortFeederName)
						.collect(Collectors.toList());
				context.putVar("deviceList", holder.getDeviceList());
				context.putVar("sheetNames", collect);
				context.putVar("companyName", holder.getCompanyName());
				context.putVar("image", Base64.getDecoder().decode(holder.getEncodedImage().getBytes()));

				JxlsHelper instance = JxlsHelper.getInstance();
				instance.setDeleteTemplateSheet(true);
				instance.setHideTemplateSheet(true);
				instance.setProcessFormulas(true);
				instance.processTemplate(is, os, context);
			}
		}
	}

	private static List<DailyReportModel> populateDeviceList() {
		List<DailyReportModel> arrayList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			DailyReportModel reportModel = new DailyReportModel("Feeder " + i, i,
					DateUtil.getFormattedTime(System.currentTimeMillis(), DateUtil.DD_MM_YY));
			
			

			arrayList.add(reportModel);
		}

		return arrayList;
	}

}
