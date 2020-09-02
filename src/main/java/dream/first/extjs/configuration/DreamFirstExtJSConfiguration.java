/**
 * 
 */
package dream.first.extjs.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import dream.first.core.DreamFirstCore;
import dream.first.core.platform.service.manage.ModuleServiceManager;
import dream.first.extjs.DreamFirstExtJS;
import dream.first.extjs.exception.DefaultExceptionResolver;
import dream.first.extjs.login.LoginInterceptor;
import dream.first.extjs.login.TestLoginInterceptor;
import dream.first.extjs.path.ModuleServiceSetServletContextListener;
import dream.first.extjs.path.ProjectPathSetServletContextListener;
import dream.first.extjs.platform.service.servlet.DefaultServletContextSetModuleService;
import dream.first.extjs.platform.service.servlet.ServletContextSetModuleService;

/**
 * 配置平台 bean
 * 
 * @since 1.0.0
 */
public class DreamFirstExtJSConfiguration {

	/**
	 * @return 默认的异常处理器
	 */
	@Bean
	@ConditionalOnMissingBean(DefaultExceptionResolver.class)
	public DefaultExceptionResolver defaultExceptionResolver() {
		return new DefaultExceptionResolver();
	}

	/**
	 * @return servlet 上下文设置模块服务
	 */
	@Bean
	@ConditionalOnMissingBean(ServletContextSetModuleService.class)
	public ServletContextSetModuleService ServletContextSetModuleService(ModuleServiceManager moduleServiceManager) {
		return new DefaultServletContextSetModuleService(moduleServiceManager);
	}

	/**
	 * @return 模块服务设置Servlet上下文监听
	 */
	@Bean
	@ConditionalOnProperty(prefix = DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX, name = "moduleServiceSetListener", havingValue = "true", matchIfMissing = false)
	@ConditionalOnMissingBean(ModuleServiceSetServletContextListener.class)
	public ModuleServiceSetServletContextListener moduleServiceSetServletContextListener() {
		return new ModuleServiceSetServletContextListener();
	}

	/**
	 * @return 项目路径设置Servlet上下文监听
	 */
	@Bean
	@ConditionalOnProperty(prefix = DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX, name = "projectPathSetListener", havingValue = "true", matchIfMissing = false)
	@ConditionalOnMissingBean(ProjectPathSetServletContextListener.class)
	public ProjectPathSetServletContextListener projectPathSetServletContextListener() {
		return new ProjectPathSetServletContextListener();
	}

	/**
	 * 登录session会话验证模式
	 * 
	 * @return session拦截器
	 */
	@Bean
	@ConditionalOnProperty(prefix = DreamFirstCore.DREAM_FIRST_PROPERTIES_PREFIX, name = "loginMode", havingValue = "session", matchIfMissing = false)
	@Order(10000)
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}

	/**
	 * @return 开发人员开发时的模式。取消登录验证等
	 */
	@Bean
	@ConditionalOnProperty(prefix = DreamFirstCore.DREAM_FIRST_PROPERTIES_PREFIX, name = "loginMode", havingValue = "test", matchIfMissing = false)
	@Order(10000)
	public TestLoginInterceptor testLoginCheckInterceptor() {
		return new TestLoginInterceptor();
	}

}
