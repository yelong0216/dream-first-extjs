package dream.first.extjs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.yelong.support.spring.mvc.exception.ViewResponseExceptionResolver;

import dream.first.extjs.support.msg.JsonMsg;

/**
 * 默认实现
 * 
 * @since 2.1
 */
public class DefaultViewResponseExceptionResolver implements ViewResponseExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultViewResponseExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception ex) {
		LOGGER.error("VIEW异常处理", ex);
		String message = ex.getMessage();
		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		mav.addAllObjects(new JsonMsg(false, message));
		return mav;
	}

}
