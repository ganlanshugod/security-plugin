/**
 * 
 */
package org.bana.springboot.plugin.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.bana.springboot.plugin.RestResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liuwenjie
 *
 */
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BanaErrorController extends AbstractErrorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(BanaErrorController.class);
	
	private ErrorProperties errorProperties;

	public BanaErrorController(ErrorAttributes errorAttributes,
			ErrorProperties errorProperties) {
		this(errorAttributes, errorProperties,
				Collections.<ErrorViewResolver>emptyList());
	}

	/**
	 * Create a new {@link BasicErrorController} instance.
	 * @param errorAttributes the error attributes
	 * @param errorProperties configuration properties
	 * @param errorViewResolvers error view resolvers
	 */
	public BanaErrorController(ErrorAttributes errorAttributes,
			ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorViewResolvers);
		Assert.notNull(errorProperties, "ErrorProperties must not be null");
		this.errorProperties = errorProperties;
	}
	
	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request,
			HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
				request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
	}
	
	/// 注入使用自定义的返回结果的配置
/*	@RequestMapping
	@ResponseBody
	public RestResponseResult errorRest(HttpServletRequest request) {
		LOG.info("使用自定义的返回错误结果的controller");
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new RestResponseResult(body);
	}*/
	
	@RequestMapping
	@ResponseBody
	public ResponseEntity<? extends Object> error(HttpServletRequest request) {
		LOG.info("使用自定义的返回错误结果的controller");
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
//		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		String exceptionName = (String)body.get("exception");
		//如果是bindResult的异常，比如参数绑定，那么我们就把他异常进行重新构造
		if(BindException.class.getName().equals(exceptionName)){
			LOG.info("BindResult类型的处理方式");
			@SuppressWarnings("unchecked")
			List<ObjectError> errorList = (List<ObjectError>)body.get("errors");
			List<String> errorMessage = new ArrayList<String>();
			for (ObjectError objectError : errorList) {
				errorMessage.add(objectError.getDefaultMessage());
				System.out.println(objectError.getObjectName());
			}
			body.put("errors", errorMessage.toString());
			return new ResponseEntity<RestResponseResult>(coverToRestResponse(body), HttpStatus.OK);
		}else if(ConstraintViolationException.class.getName().equals(exceptionName)){
			//body中的信息是再自定义的BanaDefaultErrorAttributes 中处理的
			return new ResponseEntity<RestResponseResult>(coverToRestResponse(body), HttpStatus.OK);
		}else{
			LOG.info("默认的处理方式");
			HttpStatus status = getStatus(request);
			return new ResponseEntity<Map<String, Object>>(body, status);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.web.ErrorController#getErrorPath()
	 */
	@Override
	public String getErrorPath() {
		return errorProperties.getPath();
	}
	
	private RestResponseResult coverToRestResponse(Map<String,Object> body){
		RestResponseResult result = new RestResponseResult();
		result.setResultCode((Integer)body.get("status")+"");
		Object errors = body.get("errors");
		if(errors instanceof String){
			result.setErrorMessage((String)errors);
		}else{
			result.setResultBean(errors);
		}
		return result;
	}
	
	
	protected boolean isIncludeStackTrace(HttpServletRequest request,
			MediaType produces) {
		IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
		if (include == IncludeStacktrace.ALWAYS) {
			return true;
		}
		if (include == IncludeStacktrace.ON_TRACE_PARAM) {
			return getTraceParameter(request);
		}
		return false;
	}
	
	protected ErrorProperties getErrorProperties() {
		return this.errorProperties;
	}

}
