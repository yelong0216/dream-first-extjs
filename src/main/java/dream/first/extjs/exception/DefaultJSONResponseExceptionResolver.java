package dream.first.extjs.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.yelong.core.model.manage.FieldAndColumn;
import org.yelong.core.model.validator.exception.FieldAndColumnValidateExcpetion;
import org.yelong.core.model.validator.exception.FieldValueBlankException;
import org.yelong.core.model.validator.exception.FieldValueLengthException;
import org.yelong.core.model.validator.exception.FieldValueNullException;
import org.yelong.support.spring.mvc.exception.JSONResponseExceptionResolver;

import dream.first.core.exception.RequestException;
import dream.first.extjs.support.msg.JsonMsg;

/**
 * 默认实现
 * 
 * @since 2.1
 */
public class DefaultJSONResponseExceptionResolver implements JSONResponseExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJSONResponseExceptionResolver.class);

	public static final String DEFAULT_EXCEPTION_MESSAGE = "系统开小差了!";

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception ex) {
		LOGGER.error("JSON异常处理", ex);
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
			message = DEFAULT_EXCEPTION_MESSAGE;
		}
		ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
		return mav.addAllObjects(new JsonMsg(false, message));
	}

}
