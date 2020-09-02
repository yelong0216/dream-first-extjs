package dream.first.extjs.exception.custom;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义异常控制器
 * 
 * @since 2.0
 */
public interface CustomErrorController extends ErrorController {

	String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH)
	public ModelAndView handleError() throws Exception;

	@Override
	default String getErrorPath() {
		return ERROR_PATH;
	}

}
