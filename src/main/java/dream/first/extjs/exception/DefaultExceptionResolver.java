/**
 * 
 */
package dream.first.extjs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.yelong.support.spring.mvc.exception.AbstractHandlerMethodExceptionResolverByResponseWay;
import org.yelong.support.spring.mvc.exception.JSONResponseExceptionResolver;
import org.yelong.support.spring.mvc.exception.ViewResponseExceptionResolver;

/**
 * 异常解析器
 * 
 * @since 2.1
 */
public class DefaultExceptionResolver extends AbstractHandlerMethodExceptionResolverByResponseWay {

	public DefaultExceptionResolver(JSONResponseExceptionResolver jsonResponseExceptionResolver,
			ViewResponseExceptionResolver viewResponseExceptionResolver) {
		super(jsonResponseExceptionResolver, viewResponseExceptionResolver);
	}

	@Override
	protected ModelAndView indeterminationHandlerWayExceptionResolve(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
		return viewHandlerWayExceptionResolve(request, response, handlerMethod, ex);
	}

}
