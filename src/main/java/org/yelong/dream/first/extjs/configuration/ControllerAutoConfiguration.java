/**
 * 
 */
package org.yelong.dream.first.extjs.configuration;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.yelong.dream.first.extjs.DreamFirstExtjs;
import org.yelong.dream.first.extjs.platform.dict.controller.DictTemplateController;
import org.yelong.dream.first.extjs.platform.icon.controller.IconTemplateController;
import org.yelong.dream.first.extjs.platform.service.controller.ModuleServiceInterfaceTemplateController;

/**
 * 控制器自动配置
 * 
 * @author PengFei
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(prefix = DreamFirstExtjs.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX,
name = "templateController",
havingValue = "true",
matchIfMissing = true)
public class ControllerAutoConfiguration implements ApplicationContextAware{

	private ApplicationContext applicationContext;

	@Resource
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@PostConstruct
	public void addController() throws Exception {
		addDictCondiguration();
		addIconController();
		addModuleServiceInterfaceController();
	}
	
	public void addDictCondiguration() throws Exception {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		// 这里通过builder直接生成了mycontrooler的definition，然后注册进去
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DictTemplateController.class);
		defaultListableBeanFactory.registerBeanDefinition("dictTemplateController", beanDefinitionBuilder.getBeanDefinition());
		Method method=requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod("detectHandlerMethods",Object.class);
		method.setAccessible(true);
		method.invoke(requestMappingHandlerMapping,"dictTemplateController");
	}
	
	public void addIconController() throws Exception {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		// 这里通过builder直接生成了mycontrooler的definition，然后注册进去
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(IconTemplateController.class);
		defaultListableBeanFactory.registerBeanDefinition("iconTemplateController", beanDefinitionBuilder.getBeanDefinition());
		Method method=requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod("detectHandlerMethods",Object.class);
		method.setAccessible(true);
		method.invoke(requestMappingHandlerMapping,"iconTemplateController");
	}
	
	public void addModuleServiceInterfaceController() throws Exception {
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		// 这里通过builder直接生成了mycontrooler的definition，然后注册进去
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ModuleServiceInterfaceTemplateController.class);
		defaultListableBeanFactory.registerBeanDefinition("moduleServiceInterfaceTemplateController", beanDefinitionBuilder.getBeanDefinition());
		Method method=requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod("detectHandlerMethods",Object.class);
		method.setAccessible(true);
		method.invoke(requestMappingHandlerMapping,"moduleServiceInterfaceTemplateController");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
