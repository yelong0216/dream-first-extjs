package dream.first.extjs.platform.service.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dream.first.core.controller.BaseCoreController;
import dream.first.extjs.platform.service.code.ModuleServiceInterfaceCodeManager;

/**
 * @since 1.0.0
 */
@Controller
public class ModuleServiceInterfaceCodeController extends BaseCoreController {

	@Resource
	private ModuleServiceInterfaceCodeManager moduleServiceInterfaceCodeManager;

	/**
	 * 模块服务接口JS代码
	 * 
	 * @return 模块服务接口JS代码
	 * @throws Exception 生成模块服务接口代码异常
	 */
	@ResponseBody
	@RequestMapping(value = "moduleServiceInterface/getModuleServiceInterfaceURL")
	public String getModuleServiceInterfaceURL() throws Exception {
		String serviceNames = getParameter("serviceNames");
		String serviceInterfaceNames = getParameter("serviceInterfaceNames");
		return moduleServiceInterfaceCodeManager.getJSCode(
				StringUtils.isBlank(serviceNames) ? null : serviceNames.split(","),
				StringUtils.isBlank(serviceInterfaceNames) ? null : serviceInterfaceNames.split(","));
	}

}
