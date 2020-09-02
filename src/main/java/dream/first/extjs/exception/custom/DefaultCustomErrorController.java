/**
 * 
 */
package dream.first.extjs.exception.custom;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dream.first.core.controller.BaseCoreController;

/**
 * @since 2.0
 */
@Controller
public class DefaultCustomErrorController extends BaseCoreController implements CustomErrorController {

	@Value("${project.errorViewPath:/error/404.jsp}")
	private String errorViewPath;

	@RequestMapping(value = ERROR_PATH)
	public ModelAndView handleError() throws IOException {
		HttpServletRequest request = getRequest();
		String method = request.getMethod();
		if (method.equalsIgnoreCase("get")) {
			return new ModelAndView(errorViewPath);
		} else {
			ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
			mav.addObject("success", false);
			mav.addObject("msg", "服务端异常，数据加载失败！");
			return mav;
		}
	}

}
