/**
 * 
 */
package org.bana.springboot.plugin.error;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuwenjie
 *
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnWebApplication
public class BanaExceptionAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(value = BanaErrorController.class, search = SearchStrategy.CURRENT)
	public BanaErrorController basicErrorController(ErrorAttributes errorAttributes,ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
		return new BanaErrorController(errorAttributes, serverProperties.getError(),
				errorViewResolvers);
	}
	
	@Bean
	@ConditionalOnMissingBean(value = BanaDefaultErrorAttributes.class, search = SearchStrategy.CURRENT)
	public BanaDefaultErrorAttributes defaultBanaErrorAttributes(){
		return new BanaDefaultErrorAttributes();
	}
}
