/**
 * 
 */
package org.bana.springboot.plugin.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

/**
 * @author liuwenjie
 *
 */
public class BanaDefaultErrorAttributes extends DefaultErrorAttributes{

	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
			boolean includeStackTrace) {
		Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
		dealSomeExceptionMessage(requestAttributes,errorAttributes);
		return errorAttributes;
	}

	/**
	 * @param requestAttributes 
	 * @param errorAttributes
	 */
	private void dealSomeExceptionMessage(RequestAttributes requestAttributes, Map<String, Object> errorAttributes) {
		Throwable error = getError(requestAttributes);
		if(error instanceof ConstraintViolationException){
			ConstraintViolationException exception = (ConstraintViolationException)error;
			List<String> messageList = new ArrayList<String>();
			Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
			if(constraintViolations != null){
				for (ConstraintViolation<?> constraintViolation : constraintViolations) {
					messageList.add(constraintViolation.getMessage());
				}
			}
			if(!messageList.isEmpty()){
				errorAttributes.put("message", messageList.toString());
			}
		}
	}
}
