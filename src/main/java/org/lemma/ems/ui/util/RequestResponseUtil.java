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
		SUCCESS(0);
		int code;

		private ResponseCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	public static enum ResponseDesc {
		SUCCESS("Success");
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

}
