package dream.first.extjs.spring.boot;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.yelong.spring.boot.YelongApplication;

public class ExtJSApplication extends YelongApplication {

	public ExtJSApplication(Class<?>... primarySources) {
		this(null, primarySources);
	}

	public ExtJSApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		super(resourceLoader, ArrayUtils.add(primarySources, ExtJSApplicationPrimarySource.class));
	}

	public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
		return run(new Class<?>[] { primarySource }, args);
	}

	public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
		return new ExtJSApplication(primarySources).run(args);
	}

}
