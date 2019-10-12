package org.lemma.ems.notification.util;

import java.io.InputStream;

/**
 * @author RTS Sathish Kumar
 *
 */
public class Attachment {

	private String attachmentName;;
	private InputStream stream;

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
}
