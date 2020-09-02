/**
 * 
 */
package dream.first.extjs.platform.service.servlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.util.CollectionUtils;
import org.yelong.commons.support.Entry;

import dream.first.core.platform.service.manage.ModuleServiceManager;
import dream.first.core.platform.service.model.ModuleService;

/**
 * 
 * @since 2.0
 */
public class DefaultServletContextSetModuleService implements ServletContextSetModuleService {

	protected final ModuleServiceManager moduleServiceManager;

	protected static final String DEFAULT_MODULE_SERVICE_ATTRIBUTE_NAME_TEMPLATE = "MODULE_SERVICE_${serviceName}_ROOT_PATH";

	protected static final String DEFAULT_MODULE_SERVICE_ATTRIBUTE_NAME_TEMPLATE_PLACEHOLDER = "${serviceName}";

	public DefaultServletContextSetModuleService(ModuleServiceManager moduleServiceManager) {
		this.moduleServiceManager = moduleServiceManager;
	}

	@Override
	public List<Entry<String, String>> setModuleService(ServletContext servletContext) {
		List<ModuleService> moduleServices = moduleServiceManager.getServiceAll();
		if (CollectionUtils.isEmpty(moduleServices)) {
			return Collections.emptyList();
		}
		List<Entry<String, String>> entrys = new ArrayList<Entry<String, String>>(moduleServices.size());
		for (ModuleService moduleService : moduleServices) {
			entrys.add(putModuleService(servletContext, moduleService));
		}
		return entrys;
	}

	protected Entry<String, String> putModuleService(ServletContext servletContext, ModuleService moduleService) {
		String serviceNameEn = moduleService.getServiceNameEn();
		String baseUrl = moduleService.getBaseUrl();
		String attributeName = generateModuleServiceAttributeName(serviceNameEn);
		servletContext.setAttribute(attributeName, baseUrl);
		return new Entry<String, String>(attributeName, baseUrl);
	}

	protected String generateModuleServiceAttributeName(String serviceNameEn) {
		return DEFAULT_MODULE_SERVICE_ATTRIBUTE_NAME_TEMPLATE
				.replace(DEFAULT_MODULE_SERVICE_ATTRIBUTE_NAME_TEMPLATE_PLACEHOLDER, serviceNameEn.toUpperCase());
	}

}
