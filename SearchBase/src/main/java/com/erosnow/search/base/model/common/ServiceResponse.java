package com.erosnow.search.base.model.common;

import java.io.Serializable;

public class ServiceResponse implements Serializable {
	private static final long serialVersionUID = -7409175798355390254L;
	private String code;
	private String message;
	private Object object;

	public enum ResponseStatus {
		SUCCESS("success"), FAIL("fail");
		private String responseType;

		private ResponseStatus(String responseType) {
			this.responseType = responseType;
		}

		public String responseType() {
			return responseType;
		}
	}

	public ServiceResponse() {

	}

	public ServiceResponse(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	public ServiceResponse(final String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
