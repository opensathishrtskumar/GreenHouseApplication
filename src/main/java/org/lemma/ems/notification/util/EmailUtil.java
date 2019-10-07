package org.lemma.ems.notification.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.lemma.ems.UI.dto.AttachmentDTO;
import org.lemma.ems.UI.dto.EmailDTO;
import org.lemma.ems.constants.EmailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EmailUtil {

	private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

	private static Session getEmailSession(final EmailDTO emailDTO) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", emailDTO.getSmtpHost());
		props.put("mail.smtp.port", emailDTO.getPort());
		props.put("mail.smtp.socketFactory.class",
			      "javax.net.ssl.SSLSocketFactory");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailDTO.getFromEmail(), emailDTO.getMailPassword());
			}
		});

		return session;
	}

	public static boolean sendEmail(final EmailDTO emailDTO) {

		boolean mailStatus = false;
		try {
			Message emailMessage = createMimeMessage(emailDTO);
			
			//Triggers send mail
			Transport.send(emailMessage);
			
			mailStatus = true;
		} catch (Exception e) {
			logger.error("{}", e);
		}

		logger.info("Mail message triggered for request {} with status {}", emailDTO, mailStatus);

		return mailStatus;
	}

	/**
	 * @param emailDTO
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 * 
	 *             Creates Multipart mime message
	 */
	private static Message createMimeMessage(final EmailDTO emailDTO) throws AddressException, MessagingException {

		Session session = getEmailSession(emailDTO);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailDTO.getFromEmail()));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDTO.getToEmail()));

		if (emailDTO.getCcEmail() != null) {
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailDTO.getCcEmail()));
		}
		
		if (emailDTO.getBccEmail() != null) {
			message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailDTO.getBccEmail()));
		}

		message.setSubject(emailDTO.getSubject());
		// Create Multipart mail message
		Multipart multipart = new MimeMultipart();
		
		// Create Email body
		BodyPart messageBodyPart = new MimeBodyPart();
		//messageBodyPart.setText(emailDTO.getBody());
		//For HTML emails
		messageBodyPart.setContent(emailDTO.getBody(), "text/html");
		multipart.addBodyPart(messageBodyPart);
		

		// Attach files if any required
		List<AttachmentDTO> attachments = emailDTO.getAttachments();

		if (attachments != null) {
			for (AttachmentDTO attachment : attachments) {
				
				if(attachment.getFile() == null || attachment.getFileName() == null)
					continue;
				
				BodyPart mailAttachment = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment.getFile());
				mailAttachment.setDataHandler(new DataHandler(source));
				mailAttachment.setFileName(attachment.getFileName());
				multipart.addBodyPart(mailAttachment);
			}
		}

		// Add content to final message
		message.setContent(multipart);

		return message;
	}
	
	public static String getEmailBody(String template, Map<String, String> values){
		
		logger.debug("{} {}",template,values);
		
		if(template != null && values != null){
			for(Entry<String, String> entry : values.entrySet()){
				template = template.replace(entry.getKey(), entry.getValue());
			}
		}
		
		return template;
	}
	
	public static EmailDTO setEmailDetails(EmailDTO dto){
		dto.setSubject(dto.getSubject());
		Map<String, String> values = new HashMap<>();
		values.put(EmailConstants.COMPANY_NAME_PARAM, dto.getCompanyName());
		values.put(EmailConstants.REPORT_DATE, dto.getDate());
		dto.setBody(EmailUtil.getEmailBody(dto.getBody(), values));
		return dto;
	}
	
}
