package dream.first.extjs.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import dream.first.extjs.scan.DreamFirstExtJSScan;

@SpringBootApplication
@ComponentScan(basePackages = DreamFirstExtJSScan.PACKAGE_NAME)
public class ExtJSApplicationPrimarySource {

}