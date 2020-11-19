/**
 * 
 */
package dream.first.extjs.configuration;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.yelong.support.spring.mvc.exception.JSONResponseExceptionResolver;
import org.yelong.support.spring.mvc.exception.ViewResponseExceptionResolver;

import dream.first.core.DreamFirstCore;
import dream.first.core.platform.service.manage.ModuleServiceManager;
import dream.first.extjs.DreamFirstExtJS;
import dream.first.extjs.exception.DefaultExceptionResolver;
import dream.first.extjs.exception.DefaultJSONResponseExceptionResolver;
import dream.first.extjs.exception.DefaultViewResponseExceptionResolver;
import dream.first.extjs.exception.error.DefaultErrorViewResolver;
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
@Configuration
public class DreamFirstExtJSConfiguration {

	@Bean
	@Order()
	public DefaultErrorViewResolver defaultErrorViewResolver() {
		return new DefaultErrorViewResolver();
	}

	// ==================================================异常解析器==================================================

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	@ConditionalOnMissingBean(ViewResponseExceptionResolver.class)
	public ViewResponseExceptionResolver defaultViewResponseExceptionResolver() {
		return new DefaultViewResponseExceptionResolver();
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	@ConditionalOnMissingBean(JSONResponseExceptionResolver.class)
	public JSONResponseExceptionResolver defaultJSONResponseExceptionResolver() {
		return new DefaultJSONResponseExceptionResolver();
	}

	/**
	 * {@link #ConditionalOnMissingBean} 注解没有起作用，所有将所有的都注入进来，然后根据排序取第一个
	 * 
	 * @return 默认的异常处理器
	 */
	@Bean
	@ConditionalOnMissingBean(DefaultExceptionResolver.class)
	public DefaultExceptionResolver defaultExceptionResolver(
			List<ViewResponseExceptionResolver> viewResponseExceptionResolvers,
			List<JSONResponseExceptionResolver> jsonResponseExceptionResolvers) {
		return new DefaultExceptionResolver(jsonResponseExceptionResolvers.get(0),
				viewResponseExceptionResolvers.get(0));
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
