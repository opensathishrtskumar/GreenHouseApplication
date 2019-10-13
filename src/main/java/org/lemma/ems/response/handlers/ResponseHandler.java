package org.lemma.ems.response.handlers;

import org.lemma.ems.base.dao.dto.ExtendedSerialParameter;

public interface ResponseHandler {
	public void preStart();
	public void handleResponse(ExtendedSerialParameter parameter);
	public void postStop();
}
