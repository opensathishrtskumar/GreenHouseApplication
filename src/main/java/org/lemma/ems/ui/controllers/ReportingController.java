package org.lemma.ems.ui.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.lemma.ems.service.ReportManagementService;
import org.lemma.ems.ui.model.DateRangeReportForm;
import org.lemma.ems.ui.model.ReportManagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportingController {

	private static final Logger logger = LoggerFactory.getLogger(ReportingController.class);

	@Autowired
	private ReportManagementService reportService;

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports", method = RequestMethod.GET)
	public ModelAndView showReportsPage() {
		return new ModelAndView("reports");
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/management", method = RequestMethod.GET)
	public ModelAndView showManageReports() {
		return reportService.showReportManagementPage();
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/management", method = RequestMethod.POST)
	public ModelAndView updateReportsMaster(@ModelAttribute("reportManagementForm") ReportManagementForm form) {
		return reportService.updateReportMaster(form);
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.GET)
	public ModelAndView getDateRangeReportsPage() {
		return reportService.getDateRageReportPage();
	}

	/**
	 * @param form
	 * @param formBinding
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/ems/reports/daterange", method = RequestMethod.POST)
	public void postDateRangeReportsPage(@Valid DateRangeReportForm form, BindingResult formBinding,
			HttpServletResponse response, HttpServletRequest request) throws Exception {

		if (formBinding.hasErrors()) {
			logger.error("Form has error !!!");
			String errorMessage = "Sorry. Invalid input details for report";
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}

		File file = new File("C:\\Users\\user\\Downloads\\project bms.xlsx");

		String fileName = file.getName();

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);

		response.setHeader("Content-Disposition", String.format("inline; filename=%s", fileName));

		response.setContentLength((int) file.length());

		InputStream inputStream = new FileInputStream(file);

		// Copy bytes from source to destination(outputstream in this example), closes
		// both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	@RequestMapping(value = "/ems/reports/dummy", method = RequestMethod.GET)
	public ModelAndView insertDummmyRecords() {
		return reportService.insertDummy();
	}

}