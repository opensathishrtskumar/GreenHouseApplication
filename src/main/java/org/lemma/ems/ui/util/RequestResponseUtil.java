package org.lemma.ems.ui.util;

import org.lemma.ems.ui.model.BaseResponse;

/**
 * @author RTS Sathish Kumar
 *
 */
public class RequestResponseUtil {

	/**
	 * 
	 *
	 */
	public static enum ResponseCode {
		SUCCESS(0), FAILURE(-1);
		int code;

		private ResponseCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	public static enum ResponseDesc {
		SUCCESS("Success"), FAILURE("Failure");
		String desc;

		private ResponseDesc(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static BaseResponse generateSuccessResponse(BaseResponse response) {
		response.setStatusCode(ResponseCode.SUCCESS.getCode());
		response.setStatusDescription(ResponseDesc.SUCCESS.getDesc());
		return response;
	}

	public static BaseResponse generateFailureResponse(BaseResponse response, Exception e) {
		response.setStatusCode(ResponseCode.FAILURE.getCode());

		if (e == null)
			response.setStatusDescription(ResponseDesc.FAILURE.getDesc());
		else
			response.setStatusDescription(e.getMessage());

		return response;
	}
}
