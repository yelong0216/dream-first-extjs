/**
 * 
 */
package dream.first.extjs.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yelong.support.spring.mvc.interceptor.AbstractHandlerInterceptor;

import dream.first.core.login.CurrentLoginUserInfo;
import dream.first.core.login.CurrentLoginUserInfoHolder;
import dream.first.core.platform.org.model.Org;
import dream.first.core.platform.user.constants.UserInitState;
import dream.first.core.platform.user.model.User;

/**
 * 测试登录拦截器。一般用于需要登录的系统测试的时候注入，可以在不登陆时进行访问系统
 * 
 * @since 1.0
 */
public class TestLoginInterceptor extends AbstractHandlerInterceptor {

	private static final CurrentLoginUserInfo CURRENT_LOGIN_USER_INFO;

	static {
		CURRENT_LOGIN_USER_INFO = new CurrentLoginUserInfo();
		User user = new User();
		user.setId("1");
		user.setIsSuper("1");
		user.setUsername("projectManage");
		user.setRealName("项目开发人员");
		user.setOrgId("1");
		user.setInitState(UserInitState.HAS_BEEN_INIT.CODE);

		Org org = new Org();
		org.setId("1");
		org.setOrgNo("-1");
		org.setOrgName("项目开发人员");
		CURRENT_LOGIN_USER_INFO.setOrg(org);
		CURRENT_LOGIN_USER_INFO.setUser(user);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoginSessionUser.setLoginUserInfo(request.getSession(), CURRENT_LOGIN_USER_INFO);
		CurrentLoginUserInfoHolder.setCurrentLoginUserInfo(CURRENT_LOGIN_USER_INFO);
		return true;
	}

}
