/**
 * 
 */
package org.yelong.dream.first.extjs.platform.icon.ftl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.core.model.service.ModelService;
import org.yelong.dream.first.core.platform.icon.model.Icon;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import com.google.gson.Gson;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * @author PengFei
 * @since 1.0.0
 */
public class IconFreeMarkerSupport {

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory.createConfigurationByClass(IconFreeMarkerSupport.class);

	private static final String ICON_FTL_NAME = "icon.ftl";

	private static final String ICON_CSS_FTL_NAME = "iconCss.ftl";

	@Resource
	private ModelService modelService;

	/**
	 * 生成内容
	 * @return css 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 * @throws TemplateException 
	 */
	public String generateContent() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		List<Icon> iconList = modelService.findAll(Icon.class);
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("iconData", new Gson().toJson(iconList));
		Template dictTemplate = getIconTemplate();
		return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, params);
	}

	/**
	 * 生成icon css
	 * @return css
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String generateIconCss() throws IOException, TemplateException {
		List<Icon> iconList = modelService.findAll(Icon.class);
		StringBuilder iconCss = new StringBuilder();
		for (Icon icon : iconList) {
			iconCss.append("\n")
			.append("."+icon.getIconClass()+" {\n")
			.append("\tbackground-image : url("+icon.getIconPath()+") !important;")
			.append("\tbackground-size : 100% 100%;")
			.append("\n}\n");
		}
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("iconCss",iconCss.toString());
		Template dictTemplate = getIconCssTemplate();
		return FreeMarkerTemplateUtils.processTemplateIntoString(dictTemplate, params);
	}
	
	/**
	 * 获取模板
	 * @return 模板
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	public Template getIconTemplate() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(ICON_FTL_NAME,"UTF-8");
	}
	
	/**
	 * 获取模板
	 * @return 模板
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	public Template getIconCssTemplate() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(ICON_CSS_FTL_NAME,"UTF-8");
	}


}
