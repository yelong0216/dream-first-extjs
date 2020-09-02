/**
 * 
 */
package dream.first.extjs.path;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Value;

import dream.first.core.DFApplication;
import dream.first.core.platform.service.model.ModuleService;
import dream.first.core.platform.service.service.ModuleServiceCommonService;
import dream.first.extjs.DreamFirstExtJS;

/**
 * 
 * 设置项目的静态资源根路径及其他路径配置
 * 
 * @author PengFei
 */
public class ProjectPathSetServletContextListener implements ServletContextListener {

	/**
	 * 静态资源根路径
	 */
	public static final String STATIC_RESOURCES_ROOT_PATH_NAME = "STATIC_RESOURCES_ROOT_PATH";

	/**
	 * 本项目根路径
	 */
	public static final String THIS_PROJECT_ROOT_PATH_NAME = "THIS_PROJECT_ROOT_PATH";

	@Resource
	private ModuleServiceCommonService moduleServiceCommonService;

	@Value("${" + DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX + ".staticResourcesServiceName:}")
	private String staticResourcesServiceName;

	@Value("${" + DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX + ".thisProjectServiceName:}")
	private String thisProjectServiceName;

	@Value("${" + DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX + ".staticResourcesRootPath:}")
	private String staticResourcesRootPath;

	@Value("${" + DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX + ".thisProjectRootPath:}")
	private String thisProjectRootPath;

	@Value("${spring.application.name}")
	private String springApplicationName;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();

		String thisProjectRootPath = this.thisProjectRootPath;
		String staticResourcesRootPath = this.staticResourcesRootPath;

		// 如果设置了路径则采取路径。否则查询设置了的模块名称，没有设置模块名称采用 spring application name
		if (isDefaultValue(thisProjectRootPath)) {
			// 如果thisProjectServiceName为默认值则使用当前spring application name
			String thisProjectServiceName = isDefaultValue(this.thisProjectServiceName) ? springApplicationName
					: this.thisProjectServiceName;
			ModuleService thisProjectModuleService = moduleServiceCommonService
					.findByServiceName(thisProjectServiceName);
			thisProjectRootPath = thisProjectModuleService == null ? "" : thisProjectModuleService.getBaseUrl();
		}

		// 如果设置了路径则采取路径。否则查询设置了的模块名称，没有设置模块名称采用 spring application name
		if (isDefaultValue(staticResourcesRootPath)) {
			String staticResourcesServiceName = isDefaultValue(this.staticResourcesServiceName) ? springApplicationName
					: this.staticResourcesServiceName;
			ModuleService staticResourcesModuleService = moduleServiceCommonService
					.findByServiceName(staticResourcesServiceName);
			staticResourcesRootPath = staticResourcesModuleService == null ? ""
					: staticResourcesModuleService.getBaseUrl();
		}
		if (thisProjectRootPath.equals("/")) {
			thisProjectRootPath = "";
		}

		if (staticResourcesRootPath.equals("/")) {
			staticResourcesRootPath = "";
		}

		DFApplication.setProperty("staticResourcesRootPath", staticResourcesRootPath);
		DFApplication.setProperty("thisProjectRootPath", thisProjectRootPath);

		servletContext.setAttribute(STATIC_RESOURCES_ROOT_PATH_NAME, staticResourcesRootPath);
		servletContext.setAttribute(THIS_PROJECT_ROOT_PATH_NAME, thisProjectRootPath);
	}

	private boolean isDefaultValue(String value) {
		return value.equals("");
	}

}
