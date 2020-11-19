/**
 * 
 */
package dream.first.extjs.support.msg;

import dream.first.extjs.base.msg.DFEJsonMsg;

/**
 * 一般内容的响应格式
 * 
 * @since 1.0.0
 */
public class JsonMsg extends DFEJsonMsg {

	public JsonMsg() {
	}

	public JsonMsg(boolean success) {
		super(success);
	}

	public JsonMsg(boolean success, String msg) {
		super(success, msg);
	}

}
