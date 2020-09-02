/**
 * 
 */
package dream.first.extjs.login;

import dream.first.core.login.CurrentLoginUserInfoHolder;

/**
 * 登录的用户信息持有者
 * 
 * @since 1.0
 * @see CurrentLoginUserInfoHolder
 */
public class LoginUserInfoHolder {

	public static void setLoginUser(LoginUserInfo user) {
		CurrentLoginUserInfoHolder.setCurrentLoginUserInfo(user);
	}

	public static LoginUserInfo currentLoginUser() {
		return (LoginUserInfo) CurrentLoginUserInfoHolder.currentLoginUserInfo();
	}
}
