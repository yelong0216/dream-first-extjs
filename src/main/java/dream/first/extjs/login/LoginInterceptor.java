/**
 * 
 */
package dream.first.extjs.login;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.method.HandlerMethod;
import org.yelong.commons.lang.annotation.AnnotationUtilsE;
import org.yelong.support.servlet.HttpServletUtils;
import org.yelong.support.spring.mvc.interceptor.AbstractHandlerInterceptor;

import dream.first.core.model.service.DreamFirstModelService;
import dream.first.core.platform.debug.Debugs;
import dream.first.core.platform.login.model.LoginSession;
import dream.first.core.platform.login.service.LoginSessionCommonService;
import dream.first.core.platform.user.model.User;
import dream.first.plugin.support.log.LogRecordUtils;

/**
 * 登录认证拦截器
 * 
 * @since 1.0
 */
public class LoginInterceptor extends AbstractHandlerInterceptor {

	@Resource
	private DreamFirstModelService modelService;

	@Resource
	private LoginSessionCommonService loginSessionService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		LoginValidate loginValidate = getLoginValidate((HandlerMethod) handler);
		if (loginValidate == null) {
			return true;
		}
		if (!loginValidate.validate()) {
			return true;
		}
		HttpSession session = request.getSession();
		LoginUserInfo loginUserInfo = (LoginUserInfo) session
				.getAttribute(LoginSessionUser.SESSION_LOGIN_USER_INFO.name());
		// 当前没有登录
		if (loginUserInfo == null) {
			throw new LoginException("用户登录超时！", 999);
		}
		// 正常情况下
		LoginUserInfoHolder.setLoginUser(loginUserInfo);
		Debugs.insert(modelService, "pf",
				"用户发送请求，登录验证处理器。请求地址【" + request.getRequestURL() + "】。当前登陆人【" + loginUserInfo.getUser().getUsername()
						+ "】。登录ip【" + HttpServletUtils.getIpAddrByNginxReverseProxy(request) + "】。sessionId【"
						+ session.getId() + "】");

		// 验证仅一个用户登录
		if (!loginValidate.singleLogin()) {
			return true;
		}
		LoginSession loginSession = loginSessionService.getByUsername(loginUserInfo.getUser().getUsername());
		if (null == loginSession) {
			return true;

		}
		String sessionId = loginSession.getSessionId();
		// 两次的会话id不相同视为不同浏览器登录
		if (!sessionId.equalsIgnoreCase(session.getId())) {
			Debugs.insert(modelService, "pf",
					"登录验证通过，但是该用户已经被挤掉了。请求地址【" + request.getRequestURL() + "】。。当前登陆人【"
							+ loginUserInfo.getUser().getUsername() + "】。登录ip【"
							+ HttpServletUtils.getIpAddrByNginxReverseProxy(request) + "】。sessionId【" + session.getId()
							+ "】。" + "\n\t用户新登录后的属性\t名称【" + loginSession.getUsername() + "】。登录ip【"
							+ loginSession.getLoginIp() + "】。sessionId【" + loginSession.getSessionId() + "】");
			if (loginValidate.singleLoginlog()) {
				User user = loginUserInfo.getUser();
				// 记录日志
				LogRecordUtils.setRecordLog(true);
				LogRecordUtils.setLogOperatorModule("登录");
				LogRecordUtils.setEventType("01");
				LogRecordUtils.setLogUserName(user.getUsername());
				LogRecordUtils.setLogDesc(user.getUsername() + "在其他浏览器登录！登录地址IP：" + loginSession.getLoginIp() + "。登录时间："
						+ DateFormatUtils.format(loginSession.getLoginTime(), "yyyy-MM-dd HH:mm:ss"));
			}

			// 清空当前用户的session
			session.removeAttribute(LoginSessionUser.SESSION_LOGIN_USER_INFO.name());
			session.removeAttribute(LoginSessionUser.SESSION_LOGIN_USER.name());
			throw new LoginException("您已在其他浏览器登录！", 910);
		}
		return true;
	}

	/**
	 * 根据处理器方法、处理器方法类、处理器方法类的父类直至Object.class中获取LoginValidate<br/>
	 * 如果没有LoginValidate则返回null
	 * 
	 * @param handlerMethod 处理器方法
	 * @return {@link LoginValidate}
	 */
	protected LoginValidate getLoginValidate(HandlerMethod handler) {
		Method method = handler.getMethod();
		if (method.isAnnotationPresent(LoginValidate.class)) {
			return method.getAnnotation(LoginValidate.class);
		}
		Class<?> c = handler.getBeanType();
		return AnnotationUtilsE.getAnnotation(c, LoginValidate.class, true);
	}
}
