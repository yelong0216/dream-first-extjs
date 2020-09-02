/**
 * 
 */
package dream.first.extjs.exception;

import dream.first.core.exception.RequestException;

/**
 * 请求404异常。 这个异常是伪装404.例如根据id查询，但是id的这项数据不存在。
 * 
 * @since 1.0.0
 */
public class Request404Exception extends RequestException {

	private static final long serialVersionUID = -3237772280957743022L;

	public static final int STATUS = 404;

	public Request404Exception() {
		super(STATUS);
	}

	public Request404Exception(String message) {
		super(STATUS, message);
	}

}
