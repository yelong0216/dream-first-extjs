/**
 * 
 */
package org.yelong.dream.first.extjs.platform.icon.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yelong.dream.first.extjs.controller.BaseExtjsController;
import org.yelong.dream.first.extjs.platform.icon.ftl.IconFreeMarkerSupport;

/**
 * 字典模板
 * @author PengFei
 * @since 1.0.0
 */
public class IconTemplateController extends BaseExtjsController{

	@Resource
	private IconFreeMarkerSupport iconFreeMarkerSupport;
	
	/**
	 * 获取 图标 js
	 * @return js脚本
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "icon/icon")
	public String icon() throws Exception {
		return iconFreeMarkerSupport.generateContent();
	}
	
	/**
	 * 获取字典css
	 * @return 字典css
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "icon/iconCss")
	public String iconCss() throws Exception {
		return iconFreeMarkerSupport.generateIconCss();
	}
	
}
