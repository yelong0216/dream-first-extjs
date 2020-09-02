/**
 * 
 */
package dream.first.extjs.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import dream.first.extjs.DreamFirstExtJS;
import dream.first.extjs.platform.dict.controller.DictCodeController;
import dream.first.extjs.platform.icon.controller.IconCodeController;
import dream.first.extjs.platform.service.controller.ModuleServiceInterfaceCodeController;

/**
 * 代码控制器自动配置
 * 
 * @since 1.0.0
 */
@ConditionalOnProperty(prefix = DreamFirstExtJS.DREAM_FIRST_EXTJS_PROPERTIES_PREFIX, name = "codeController", havingValue = "true", matchIfMissing = false)
public class CodeControllerAutoConfiguration {

	/**
	 * @return 模块服务代码控制器
	 */
	@Bean
	public ModuleServiceInterfaceCodeController moduleServiceInterfaceCodeController() {
		return new ModuleServiceInterfaceCodeController();
	}

	/**
	 * @return 字典代码控制器
	 */
	@Bean
	public DictCodeController dictCodeController() {
		return new DictCodeController();
	}

	/**
	 * @return 图标代码控制器
	 */
	@Bean
	public IconCodeController iconCodeController() {
		return new IconCodeController();
	}

}
