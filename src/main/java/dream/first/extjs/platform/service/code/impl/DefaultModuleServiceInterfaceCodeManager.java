/**
 * 
 */
package dream.first.extjs.platform.service.code.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.commons.lang.Strings;
import org.yelong.core.annotation.Nullable;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import dream.first.core.platform.service.manage.ModuleServiceManager;
import dream.first.core.platform.service.model.ModuleService;
import dream.first.core.platform.service.model.ModuleServiceInterface;
import dream.first.extjs.platform.service.code.ModuleServiceInterfaceCodeManageException;
import dream.first.extjs.platform.service.code.ModuleServiceInterfaceCodeManager;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 默认的模块管理器接口代码异常
 * 
 * @since 2.0
 */
public class DefaultModuleServiceInterfaceCodeManager implements ModuleServiceInterfaceCodeManager {

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory
			.createConfigurationByClass(DefaultModuleServiceInterfaceCodeManager.class);

	protected final ModuleServiceManager moduleServiceManager;

	public DefaultModuleServiceInterfaceCodeManager(ModuleServiceManager moduleServiceManager) {
		this.moduleServiceManager = moduleServiceManager;
	}

	@Override
	public String getJSCode() throws ModuleServiceInterfaceCodeManageException {
		return doGenerate(moduleServiceManager.getInterfaceAll());
	}

	@Override
	public String getJSCode(String[] serviceNames) throws ModuleServiceInterfaceCodeManageException {
		return generateModuleServiceInterfaceURL(serviceNames, null);
	}

	@Override
	public String getJSCode(String[] serviceNames, String[] serviceInterfaceNames)
			throws ModuleServiceInterfaceCodeManageException {
		return generateModuleServiceInterfaceURL(serviceNames, serviceInterfaceNames);
	}

	private String generateModuleServiceInterfaceURL(@Nullable String[] serviceNames,
			@Nullable String[] serviceInterfaceNames) {
		List<ModuleServiceInterface> moduleServiceInterfaces = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(serviceNames)) {
			for (String serviceName : serviceNames) {
				ModuleService moduleService = moduleServiceManager.getServiceByServiceName(serviceName);
				if (null != moduleService) {
					moduleServiceInterfaces.addAll(moduleServiceManager.getInterfaceByServiceId(moduleService.getId()));
				}
			}
		}
		if (ArrayUtils.isNotEmpty(serviceInterfaceNames)) {
			for (String serviceInterfaceName : serviceInterfaceNames) {
				ModuleServiceInterface moduleServiceInterface = moduleServiceManager
						.getInterfaceByInterfaceName(serviceInterfaceName);
				if (null != moduleServiceInterface) {
					moduleServiceInterfaces.add(moduleServiceInterface);
				}
			}
		}
		return doGenerate(moduleServiceInterfaces);
	}

	protected String doGenerate(List<ModuleServiceInterface> moduleServiceInterfaces) {
		List<Map<String, String>> moduleServiceInterfaceMapList = moduleServiceInterfaces.stream().map(x -> {
			Map<String, String> moduleServiceInterfaceMap = new HashMap<>();
			moduleServiceInterfaceMap.put("interfaceName", x.getInterfaceName());
			String interfaceUrl = moduleServiceManager.getInterfaceUrl(x.getInterfaceName());
			moduleServiceInterfaceMap.put("interfaceURL", interfaceUrl);
			return moduleServiceInterfaceMap;
		}).filter(x -> {
			if (Strings.isNullOrBlank(x.get("interfaceName"))) {
				return false;
			}
			if (Strings.isNullOrBlank(x.get("interfaceURL"))) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		Map<String, Object> context = new HashMap<>();
		context.put("moduleServiceInterfaceList", moduleServiceInterfaceMapList);
		try {
			Template template = freemarkerConfiguration.getTemplate("moduleServiceInterfaceURL.ftl", "UTF-8");
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
		} catch (Exception e) {
			throw new ModuleServiceInterfaceCodeManageException(e);
		}
	}

	@Override
	public ModuleServiceManager getModuleServiceManager() {
		return this.moduleServiceManager;
	}

}
