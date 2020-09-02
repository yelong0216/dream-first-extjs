/**
 * 
 */
package dream.first.extjs.login;

import javax.servlet.http.HttpSession;

import dream.first.core.login.CurrentLoginUserInfo;
import dream.first.core.platform.org.model.Org;
import dream.first.core.platform.user.model.User;

/**
 * 登录后用户在Session中存放的键值
 * 
 * @since 1.0
 */
public enum LoginSessionUser {

	/** 登录的用户信息 CurrentLoginUserInfo */
	SESSION_LOGIN_USER_INFO,
	// ==================================================用户信息==================================================

	/** 登录的用户 User */
	SESSION_LOGIN_USER,
	/** 登录的用户名称 */
	SESSION_LOGIN_USER_NAME,
	/** 登录的用户是否是超级用户 */
	SESSION_IS_SUPER,
	/** 登录的用户是否密码是初始密码 */
	SESSION_LOGIN_PASSWORD_INIT,
	// ==================================================组织信息==================================================
	/** 登录的用户组织id */
	SESSION_LOGIN_USER_ORGID,
	/** 登录的组织编号 */
	SESSION_LOGIN_USER_ORGNO,
	/** 登录的用户组织名称 */
	SESSION_LOGIN_USER_ORGNAME,
	// ==================================================权限信息==================================================
	/** 登录用户拥有的操作权限 */
	SESSION_LOGIN_USER_OP_RIGHTS,
	/** 登录用户拥有的数据权限 */
	SESSION_LOGIN_USER_DATA_RIGHTS;

	/**
	 * 设置登录用户信息
	 * 
	 * @param session              session
	 * @param currentLoginUserInfo 登录的用户信息
	 */
	public static void setLoginUserInfo(HttpSession session, CurrentLoginUserInfo currentLoginUserInfo) {
		session.setAttribute(SESSION_LOGIN_USER_INFO.name(), currentLoginUserInfo);
		User user = currentLoginUserInfo.getUser();
		if (null != user) {
			session.setAttribute(SESSION_LOGIN_USER.name(), user);
			session.setAttribute(SESSION_LOGIN_USER_NAME.name(), user.getUsername());
			session.setAttribute(SESSION_IS_SUPER.name(), user.getIsSuper());
			session.setAttribute(SESSION_LOGIN_PASSWORD_INIT.name(), user.getInitState());
		}
		Org org = currentLoginUserInfo.getOrg();
		if (null != org) {
			session.setAttribute(SESSION_LOGIN_USER_ORGID.name(), org.getId());
			session.setAttribute(SESSION_LOGIN_USER_ORGNO.name(), org.getOrgNo());
			session.setAttribute(SESSION_LOGIN_USER_ORGNAME.name(), org.getOrgName());
		}
		currentLoginUserInfo.setAttribute(SESSION_LOGIN_USER_OP_RIGHTS.name(), currentLoginUserInfo.getOpRights());
	}

	/**
	 * 移除登录的用户信息
	 * 
	 * @param session session
	 */
	public static void removeLoginUserInfo(HttpSession session) {
		LoginSessionUser[] loginSessionUsers = LoginSessionUser.values();
		for (LoginSessionUser loginSessionUser : loginSessionUsers) {
			session.removeAttribute(loginSessionUser.name());
		}
	}

}
