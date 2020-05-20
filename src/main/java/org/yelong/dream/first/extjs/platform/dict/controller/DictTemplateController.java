/**
 * 
 */
package org.yelong.dream.first.extjs.platform.dict.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.dream.first.core.platform.dict.service.DictCommonService;
import org.yelong.dream.first.extjs.controller.BaseExtjsController;
import org.yelong.dream.first.extjs.platform.dict.tpl.DictFreeMarkerSupport;

/**
 * 字典模板
 * @author PengFei
 * @since 1.0.0
 */
public class DictTemplateController extends BaseExtjsController{

	@Resource
	private DictCommonService dictService;

	@Resource
	private DictFreeMarkerSupport dictFreeMarkerSupport;
	
	/**
	 * 获取字典js
	 * @return 字典js
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "dict/dict")
	public String dict() throws Exception {
		String dictTypesStr = getRequest().getParameter("dictTypes");
		String [] dictTypes = StringUtils.isBlank(dictTypesStr) ? ArrayUtils.EMPTY_STRING_ARRAY:dictTypesStr.split(",");
		if( ArrayUtils.isEmpty(dictTypes) ) {
			return dictFreeMarkerSupport.generateContent();
		}
		return dictFreeMarkerSupport.generateContent(dictTypes);
	}
	
}
