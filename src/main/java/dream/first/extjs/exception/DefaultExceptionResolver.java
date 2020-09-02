/**
 * 
 */
package dream.first.extjs.exception;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.yelong.core.model.manage.FieldAndColumn;
import org.yelong.core.model.validator.exception.FieldAndColumnValidateExcpetion;
import org.yelong.core.model.validator.exception.FieldValueBlankException;
import org.yelong.core.model.validator.exception.FieldValueLengthException;
import org.yelong.core.model.validator.exception.FieldValueNullException;
import org.yelong.support.spring.mvc.exception.AbstractHandlerExceptionResolverByResponseWay;

import com.google.gson.Gson;

import dream.first.core.exception.RequestException;
import dream.first.core.gson.GsonSupplier;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * 异常解析器
 * 
 * @since 1.0.0
 */
public class DefaultExceptionResolver extends AbstractHandlerExceptionResolverByResponseWay {

	private static final Gson gson = GsonSupplier.getDefaultGson();

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionResolver.class);

	@Override
	protected String handlerExceptionResponseJson(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		String message = ex.getMessage();
		if (ex instanceof RequestException) {
			int status = ((RequestException) ex).getStatus();
			response.setStatus(status);
		}
		if (ex instanceof FieldAndColumnValidateExcpetion) {
			FieldAndColumnValidateExcpetion fieldAndColumnValidateExcpetion = ((FieldAndColumnValidateExcpetion) ex);
			FieldAndColumn fieldAndColumn = fieldAndColumnValidateExcpetion.getFieldAndColumn();
			if (ex instanceof FieldValueNullException) {
				message = fieldAndColumn.getColumnName() + "不允许为空";
			} else if (fieldAndColumnValidateExcpetion instanceof FieldValueBlankException) {
				message = fieldAndColumn.getColumnName() + "不允许空白";
			} else if (fieldAndColumnValidateExcpetion instanceof FieldValueLengthException) {
				message = fieldAndColumn.getColumnName() + "的最大长度不能超过：" + fieldAndColumn.getMaxLength() + "个字符";
			}
		}
		// 防止空指针，或者其他异常没有异常消息的。
		if (StringUtils.isBlank(message)) {
			message = "系统开小差了！";
		}
		LOGGER.error("", ex);
		return gson.toJson(new JsonMsg(false, message));
	}

	@Override
	protected ModelAndView handlerExceptionResponseModelAndView(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		LOGGER.error("", ex);
		ModelAndView mav = new ModelAndView();
		if (ex instanceof LoginException) {
			mav.setViewName("error/loginSkip.jsp");
		} /*
			 * else if(ex instanceof AccessDenialException ) {
			 * mav.setViewName("error/accessDenialError.jsp"); }
			 */else if (ex instanceof Request404Exception) {
			mav.setViewName("error/404.jsp");
		} else {
			mav.setViewName("error/error.jsp");
		}
		mav.addObject("msg", ex.getMessage());
		return mav;
	}

}
