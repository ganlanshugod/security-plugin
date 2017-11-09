package org.bana.springboot.plugin.error;

import org.bana.springboot.plugin.RestResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


public class BanaExceptionHandlerAdvice {
	
	private static final Logger LOG = LoggerFactory.getLogger(BanaExceptionHandlerAdvice.class);

	@ExceptionHandler(value=Exception.class)  
	@ResponseBody
	public RestResponseResult handleException(Exception ex) {
//		MethodArgumentNotValidException
		LOG.info("拦截到了参数验证异常",ex);
		 String errorMesssage = "Invalid Request:\n";  
		if(ex instanceof BindException){
			 BindingResult bindingResult = ((BindException)ex).getBindingResult();  
			    for (FieldError fieldError : bindingResult.getFieldErrors()) {  
			        errorMesssage += fieldError.getDefaultMessage() + "\n";  
			    } 
		}
	   
	    return new RestResponseResult("501",errorMesssage);  
	}  
}
