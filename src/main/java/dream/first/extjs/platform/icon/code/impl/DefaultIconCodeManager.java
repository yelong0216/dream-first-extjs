/**
 * 
 */
package dream.first.extjs.platform.icon.code.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.yelong.support.freemarker.FreeMarkerConfigurationFactory;

import com.google.gson.Gson;

import dream.first.core.DFApplication;
import dream.first.core.platform.icon.manage.IconManager;
import dream.first.core.platform.icon.model.Icon;
import dream.first.extjs.platform.icon.code.IconCodeManageException;
import dream.first.extjs.platform.icon.code.IconCodeManager;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * 默认实现
 * 
 * @since 2.0
 */
public class DefaultIconCodeManager implements IconCodeManager {

	private static Configuration freemarkerConfiguration = FreeMarkerConfigurationFactory
			.createConfigurationByClass(DefaultIconCodeManager.class);

	private static final String ICON_EXTJS_FTL_NAME = "icon_extjs.ftl";

	private static final String ICON_CSS_FTL_NAME = "icon_css.ftl";

	protected final IconManager iconManager;

	public DefaultIconCodeManager(IconManager iconManager) {
		this.iconManager = iconManager;
	}

	@Override
	public String getCSSCode() throws IconCodeManageException {
		List<Icon> iconList = iconManager.getAll();
		StringBuilder iconCss = new StringBuilder();
		for (Icon icon : iconList) {
			iconCss.append("\n").append("." + icon.getIconClass() + " {\n");
			String iconPath = icon.getIconPath();
			if (!iconPath.contains("http")) {
				String staticResourcesRootPath = DFApplication.getProperty("staticResourcesRootPath", "");
				if (iconPath.startsWith("/")) {
					iconPath = staticResourcesRootPath + iconPath;
				} else {
					iconPath = staticResourcesRootPath + "/" + iconPath;
				}
			}
			iconCss.append("\tbackground-image : url(" + iconPath + ") !important;")
					.append("\tbackground-size : 100% 100%;").append("\n}\n");
		}
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("iconCss", iconCss.toString());
		try {
			Template template = getIconCSSTemplate();
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
		} catch (Exception e) {
			throw new IconCodeManageException(e);
		}

	}

	@Override
	public String getExtJSCode() throws IconCodeManageException {
		List<Icon> iconList = iconManager.getAll();
		iconList = iconList.stream().map(x -> {
			String iconPath = x.getIconPath();
			if (!iconPath.contains("http")) {
				String staticResourcesRootPath = DFApplication.getProperty("staticResourcesRootPath", "");
				if (iconPath.startsWith("/")) {
					x.setIconPath(staticResourcesRootPath + iconPath);
				} else {
					x.setIconPath(staticResourcesRootPath + "/" + iconPath);
				}
			}
			return x;
		}).collect(Collectors.toList());
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("iconData", new Gson().toJson(iconList));
		try {
			Template template = getIconExtJSTemplate();
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
		} catch (Exception e) {
			throw new IconCodeManageException(e);
		}
	}

	@Override
	public IconManager getIconManager() {
		return this.iconManager;
	}

	/**
	 * 获取模板
	 */
	protected Template getIconExtJSTemplate()
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(ICON_EXTJS_FTL_NAME, "UTF-8");
	}

	/**
	 * 获取模板
	 */
	protected Template getIconCSSTemplate()
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		return freemarkerConfiguration.getTemplate(ICON_CSS_FTL_NAME, "UTF-8");
	}

}
