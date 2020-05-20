package org.yelong.dream.first.extjs.platform.service.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.dream.first.core.platform.service.dto.ModuleServiceInterfaceTemplateDTO;
import org.yelong.dream.first.extjs.controller.BaseExtjsController;
import org.yelong.dream.first.extjs.platform.service.tpl.ModuleServiceInterfaceTemplate;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author PengFei
 * @since 1.0.0
 */
public class ModuleServiceInterfaceTemplateController extends BaseExtjsController{ 

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory.createConfigurationByClass(ModuleServiceInterfaceTemplate.class);

	/**
	 * 服务接口模板
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "moduleServiceInterface/getModuleServiceInterfaceURL")
	public String getModuleServiceInterfaceURL() throws Exception{
		String serviceNames = getRequest().getParameter("serviceNames");
		String serviceInterfaceNames = getRequest().getParameter("serviceInterfaceNames");
		ModuleServiceInterfaceTemplateDTO moduleServiceInterfaceVO = new ModuleServiceInterfaceTemplateDTO();
		if(StringUtils.isNotBlank(serviceNames)) {
			moduleServiceInterfaceVO.addExtendAttribute("moduleService.serviceName",Arrays.asList(serviceNames.split(",")));
			moduleServiceInterfaceVO.addConditionOperator("moduleService.serviceName", "IN");
		}
		if(StringUtils.isNotBlank(serviceInterfaceNames)) {
			moduleServiceInterfaceVO.addExtendAttribute("moduleServiceInterface.interfaceName",Arrays.asList(serviceInterfaceNames.split(",")));
			moduleServiceInterfaceVO.addConditionOperator("moduleServiceInterface.interfaceName", "IN");
		}
		List<ModuleServiceInterfaceTemplateDTO> moduleServiceInterfaceList = modelService.findBySqlModel(ModuleServiceInterfaceTemplateDTO.class, moduleServiceInterfaceVO);
		List<Map<String,String>> moduleServiceInterfaceMapList = moduleServiceInterfaceList.stream()
				.map(x->{
					Map<String,String> moduleServiceInterfaceMap = new HashMap<>();
					moduleServiceInterfaceMap.put("interfaceName", x.getInterfaceName());
					moduleServiceInterfaceMap.put("interfaceURL", x.getServiceBaseUrl()+"/"+x.getInterfaceUrl());
					return moduleServiceInterfaceMap;
				}).collect(Collectors.toList());
		Map<String,Object> context = new HashMap<>();
		context.put("moduleServiceInterfaceList", moduleServiceInterfaceMapList);
		Template template = freemarkerConfiguration.getTemplate("ModuleServiceInterfaceURL.ftl", "UTF-8");
		String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);
		return content;
	}

}
