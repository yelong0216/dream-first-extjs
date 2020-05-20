/**
 * 
 */
package org.yelong.dream.first.extjs.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.yelong.dream.first.extjs.platform.dict.tpl.DictFreeMarkerSupport;
import org.yelong.dream.first.extjs.platform.icon.ftl.IconFreeMarkerSupport;
import org.yelong.dream.first.extjs.platform.icon.ftl.IconFreeMarkerSupportExtend;

/**
 * 平台配置
 */
public class PlatformConfiguration {

	/**
	 * @return 字典模板
	 */
	@Bean
	@ConditionalOnMissingBean(DictFreeMarkerSupport.class)
	public DictFreeMarkerSupport dictFreeMarkerSupport() {
		return new DictFreeMarkerSupport();
	}
	
	/**
	 * @return 图标模板
	 */
	@Bean
	@ConditionalOnMissingBean(IconFreeMarkerSupportExtend.class)
	public IconFreeMarkerSupport iconFreeMarkerSupport() {
		return new IconFreeMarkerSupportExtend();
	}
	
}
