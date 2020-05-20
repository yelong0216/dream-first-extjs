/**
 * 
 */
package org.yelong.dream.first.extjs.platform.icon.ftl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.core.model.service.ModelService;
import org.yelong.dream.first.core.YApplication;
import org.yelong.dream.first.core.platform.icon.model.Icon;

import com.google.gson.Gson;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author PengFei
 * @since 1.0.0
 */
public class IconFreeMarkerSupportExtend extends IconFreeMarkerSupport{

	@Resource
	private ModelService modelService;
	
	@Override
	public String generateContent() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException,
			IOException, TemplateException {
		List<Icon> iconList = modelService.findAll(Icon.class);
		iconList = iconList.stream().map(x->{
			String iconPath = x.getIconPath();
			if(!iconPath.contains("http")) {
				String staticResourcesRootPath = YApplication.getProperty("staticResourcesRootPath","");
				if(iconPath.startsWith("/")) {
					x.setIconPath(staticResourcesRootPath+iconPath);
				} else {
					x.setIconPath(staticResourcesRootPath+"/"+iconPath);
				}
			}
			return x;
		}).collect(Collectors.toList());
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("iconData", new Gson().toJson(iconList));
		Template dictTemplate = getIconTemplate();
		return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, params);
	}
	
	@Override
	public String generateIconCss() throws IOException, TemplateException {
		List<Icon> iconList = modelService.findAll(Icon.class);
		StringBuilder iconCss = new StringBuilder();
		for (Icon icon : iconList) {
			iconCss.append("\n")
			.append("."+icon.getIconClass()+" {\n");
			String iconPath = icon.getIconPath();
			if(!iconPath.contains("http")) {
				String staticResourcesRootPath = YApplication.getProperty("staticResourcesRootPath","");
				if(iconPath.startsWith("/")) {
					iconPath = staticResourcesRootPath+iconPath;
				} else {
					iconPath = staticResourcesRootPath+"/"+iconPath;
				}
			}
			iconCss.append("\tbackground-image : url("+iconPath+") !important;")
			.append("\tbackground-size : 100% 100%;")
			.append("\n}\n");
		}
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("iconCss",iconCss.toString());
		Template dictTemplate = getIconCssTemplate();
		return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, params);
	}
	
}
