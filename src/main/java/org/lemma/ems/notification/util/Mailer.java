package org.lemma.ems.notification.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

/**
 * @author RTS Sathish Kumar
 *
 */
@Component
@Lazy(true)
public class Mailer {

	private static final Logger logger = LoggerFactory.getLogger(Mailer.class);

	private static final String TEMPLATE_BASE = "/notificationtemplates/";
	private static final String MAIL_ENCODING = "UTF-8";

	@Autowired
	private JavaMailSenderImpl mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	/**
	 * @param mail
	 * @param params
	 */
	public void sendMail(Mail mail, Map<String, Object> params) {

		MimeMessage message = this.mailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(mail.getMailFrom());
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setSentDate(mail.getMailSendDate());
			if (!StringUtils.isEmpty(mail.getMailBcc()))
				mimeMessageHelper.addBcc(mail.getMailBcc());
			if (!StringUtils.isEmpty(mail.getMailCc()))
				mimeMessageHelper.addCc(mail.getMailCc());

			String content = getContentFromTemplate(params, mail.getTemplateName());
			mail.setMailContent(content);
			mimeMessageHelper.setText(mail.getMailContent(), true);

			includeAttachments(mail, mimeMessageHelper);

			mailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
			logger.error("Error sending notification {}", e);
		}
	}

	/**
	 * @param mail
	 * @param mimeMessageHelper
	 * @throws MessagingException
	 */
	private void includeAttachments(Mail mail, MimeMessageHelper mimeMessageHelper) throws MessagingException {
		if (mail.getAttachments() != null) {
			for (final Attachment attachment : mail.getAttachments()) {
				mimeMessageHelper.addAttachment(attachment.getAttachmentName(), new InputStreamSource() {
					@Override
					public InputStream getInputStream() throws IOException {
						return attachment.getStream();
					}
				});
			}
		}
	}

	/**
	 * @param model
	 * @param templateName
	 * @return
	 */
	public String getContentFromTemplate(Map<String, Object> model, String templateName) {
		StringWriter writer = new StringWriter();
		try {
			VelocityEngineUtils.mergeTemplate(velocityEngine, TEMPLATE_BASE + templateName, MAIL_ENCODING, model,
					writer);
		} catch (Exception e) {
			logger.error("Error merging notification template {}", e);
		}
		return writer.toString();
	}
}