package dream.first.extjs.platform.service.servlet;

import java.util.List;

import javax.servlet.ServletContext;

import org.yelong.commons.support.Entry;

/**
 * 上下文设置全局的模块服务服务信息
 * 
 * @since 2.0
 */
public interface ServletContextSetModuleService {

	/**
	 * 设置模块服务信息
	 * 
	 * @param servletContext servlet 上下文
	 * @return 设置的所有模块服务信息的服务名称与URL键值对
	 */
	List<Entry<String, String>> setModuleService(ServletContext servletContext);

}
