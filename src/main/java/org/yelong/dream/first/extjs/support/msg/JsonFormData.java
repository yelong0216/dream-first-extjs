/**
 * 
 */
package org.yelong.dream.first.extjs.support.msg;

/**
 * 集合性数据格式
 * 
 * @author PengFei
 * @since 1.0.0
 */
public class JsonFormData<T> {
	
	private boolean success;
	
	private T data;

	public JsonFormData() {
		
	}
	
	public JsonFormData(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
