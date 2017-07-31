package org.bana.springboot.security.plugin;

import java.io.Serializable;

public class RestResponseResult implements Serializable{

	private static final long serialVersionUID = 739293180295450345L;
	
	public static final String SUCCESS_CODE = "200";
	
	private Object resultBean;
	private String resultCode;
	
	private String errorMessage;	//返回结果信息
	private String errorStackTrack;	//返回结果的堆栈信息
	
	public RestResponseResult() {
		super();
		this.resultCode = SUCCESS_CODE;
	}

	public RestResponseResult(String resultCode, String errorMessage, String errorStackTrack) {
		super();
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
		this.errorStackTrack = errorStackTrack;
	}
	
	public RestResponseResult(String resultCode, String errorMessage) {
		super();
		this.resultCode = resultCode;
		this.errorMessage = errorMessage;
	}
	
	public RestResponseResult(Object resultBean) {
		super();
		this.resultCode = SUCCESS_CODE;
		this.resultBean = resultBean;
	}



	public Object getResultBean() {
		return resultBean;
	}
	public void setResultBean(Object resultBean) {
		this.resultBean = resultBean;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorStackTrack() {
		return errorStackTrack;
	}
	public void setErrorStackTrack(String errorStackTrack) {
		this.errorStackTrack = errorStackTrack;
	}
	
}
