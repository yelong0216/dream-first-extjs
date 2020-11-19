package dream.first.extjs.exception.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import dream.first.extjs.exception.Request404Exception;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * @since 2.1
 */
public class DefaultErrorViewResolver implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		String method = request.getMethod();
		if (method.equalsIgnoreCase("get")) {
			// 这里抛出异常之后会由异常解析器处理，并返回404页面
			throw new Request404Exception((String) model.get("path"));
		} else {
			ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
			mav.addAllObjects(new JsonMsg(false, "服务端异常，数据加载失败！"));
			return mav;
		}
	}

}
