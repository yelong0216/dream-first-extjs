/**
 * 
 */
package dream.first.extjs.platform.dict.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dream.first.core.controller.BaseCoreController;
import dream.first.extjs.login.LoginValidate;
import dream.first.extjs.platform.dict.code.DictCodeManager;

/**
 * 字典模板控制器
 * 
 * @since 1.0
 */
@LoginValidate
@Controller
public class DictCodeController extends BaseCoreController {

	@Resource
	private DictCodeManager dictExtjsManager;

	/**
	 * 获取字典JS
	 * 
	 * @return 字典JS
	 * @throws Exception 生成字典JS异常
	 */
	@ResponseBody
	@RequestMapping(value = "dict/dict")
	public String dict() throws Exception {
		String dictTypes = getRequest().getParameter("dictTypes");
		if (StringUtils.isBlank(dictTypes)) {
			return dictExtjsManager.getExtJSCode();
		} else {
			return dictExtjsManager.getExtJSCode(dictTypes.split(","));
		}
	}

}
