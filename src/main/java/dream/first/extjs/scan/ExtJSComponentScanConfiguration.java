/**
 * 
 */
package dream.first.extjs.scan;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dream.first.extjs.exception.custom.CustomErrorController;
import dream.first.extjs.exception.custom.DefaultCustomErrorController;

/**
 * 
 * @since
 */
@Configuration
public class ExtJSComponentScanConfiguration {

	@Bean
	@ConditionalOnMissingBean(value = CustomErrorController.class)
	public DefaultCustomErrorController customErrorController() {
		return new DefaultCustomErrorController();
	}

}
