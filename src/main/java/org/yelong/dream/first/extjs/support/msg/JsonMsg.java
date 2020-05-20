/**
 * 
 */
package org.yelong.dream.first.extjs.support.msg;

/**
 * 一般内容的响应格式
 * 
 * @author PengFei
 * @since 1.0.0
 */
public class JsonMsg {

	private boolean success;

	private String msg;

	private String exception;

	public JsonMsg(boolean success){
		this.success = success;
	}

	public JsonMsg(boolean success, String msg) {
		this.success = success;
		setMsg(msg);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
