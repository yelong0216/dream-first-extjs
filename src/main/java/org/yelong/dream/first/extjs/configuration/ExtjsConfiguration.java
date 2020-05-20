/**
 * 
 */
package org.yelong.dream.first.extjs.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yelong.dream.first.extjs.exception.DefaultExceptionResolver;

/**
 * 配置平台 bean
 * 
 * @author PengFei
 * @since 1.0.0
 */
@Configuration
public class ExtjsConfiguration {

	/**
	 * @return 默认的异常处理器
	 */
	@Bean
	@ConditionalOnMissingBean(DefaultExceptionResolver.class)
	public DefaultExceptionResolver defaultExceptionResolver() {
		return new DefaultExceptionResolver();
	}
	
}
