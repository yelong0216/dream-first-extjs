/**
 * 
 */
package dream.first.extjs.login;

import dream.first.core.exception.RequestException;

/**
 * 登录异常
 * 
 * @since 2.0
 */
public class LoginException extends RequestException {

	private static final long serialVersionUID = -536575856258902835L;

	public LoginException(int status) {
		super(status);
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(String message, int status) {
		super(status, message);
	}

}
