package dream.first.extjs.path;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dream.first.extjs.platform.service.servlet.ServletContextSetModuleService;

/**
 * 设置模块服务
 * 
 * @since 2.0
 */
public class ModuleServiceSetServletContextListener implements ServletContextListener {

	@Resource
	private ServletContextSetModuleService servletContextSetModuleService;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		servletContextSetModuleService.setModuleService(servletContext);
	}

}
