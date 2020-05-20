/**
 * 
 */
package org.yelong.dream.first.extjs.exception;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.yelong.dream.first.core.exception.RequestException;
import org.yelong.dream.first.extjs.support.msg.JsonMsg;
import org.yelong.support.spring.mvc.exception.AbstractHandlerExceptionResolverByResponseWay;

import com.google.gson.Gson;

/**
 * 异常解析器
 * 
 * @author PengFei
 * @since 1.0.0
 */
public class DefaultExceptionResolver extends AbstractHandlerExceptionResolverByResponseWay{

	private static final Gson gson = new Gson();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionResolver.class);
	
	@Override
	protected String handlerExceptionResponseJson(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		String json = "";
		JsonMsg jsonMsg = new JsonMsg(false,ex.getMessage());
//		jsonMsg.setException(ExceptionUtils.getStackTrace(ex));
		json = gson.toJson(jsonMsg);
		if( ex instanceof RequestException) {
			int status = ((RequestException)ex).getStatus();
			response.setStatus(status);
		}
		//防止空指针，或者其他异常没有异常消息的。
		if(StringUtils.isBlank(jsonMsg.getMsg())) {
			jsonMsg.setMsg("系统开小差了！");
		}
		LOGGER.error("", ex);
		return json;
	}

	@Override
	protected ModelAndView handlerExceptionResponseModelAndView(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		LOGGER.error("", ex);
		ModelAndView mav = new ModelAndView();
		if( ex instanceof LoginException) {
			mav.setViewName("error/loginSkip.jsp");
		} /*
			 * else if(ex instanceof AccessDenialException ) {
			 * mav.setViewName("error/accessDenialError.jsp"); }
			 */else if(ex instanceof Request404Exception ) {
			mav.setViewName("error/404.jsp");
		} else {
			mav.setViewName("error/error.jsp");
		}
		mav.addObject("msg", ex.getMessage());
		return mav;
	}
	
}
