/**
 * 
 */
package dream.first.extjs.support.msg;

import dream.first.extjs.base.msg.DFEJsonFormData;

/**
 * 集合性数据格式
 * 
 * @since 1.0.0
 */
public class JsonFormData<T> extends DFEJsonFormData<T> {

	public JsonFormData() {

	}

	public JsonFormData(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

}
