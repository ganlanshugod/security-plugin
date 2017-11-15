/**
 * 
 */
package org.bana.springboot.plugin.web;

import org.bana.springboot.plugin.web.sitemesh.SitemeshController;
import org.bana.springboot.plugin.web.sitemesh.SitemeshProperties;
import org.bana.springboot.plugin.web.sitemesh.WebSiteMeshFilter;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liuwenjie
 *
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(SitemeshProperties.class)
@Import(BanaWebMvcConfig.class)
public class BanaWebAutoConfiguration {

	/**
	 * 注册sitemesh的拦截器
	 * @return
	 */
	@ConditionalOnClass(ConfigurableSiteMeshFilter.class)
	@Bean
	public FilterRegistrationBean siteMeshFilter(SitemeshProperties sitemeshProperties) {
		FilterRegistrationBean fitler = new FilterRegistrationBean();
		WebSiteMeshFilter siteMeshFilter = new WebSiteMeshFilter();
		siteMeshFilter.setSitemeshProperties(sitemeshProperties);
		fitler.setFilter(siteMeshFilter);
		return fitler;
	}
	
	@ConditionalOnMissingBean(SitemeshController.class)
	@ConditionalOnClass(ConfigurableSiteMeshFilter.class)
	@RequestMapping("sitemesh")
	@Controller
	public class DefaultSiteMeshController extends SitemeshController{
		
	}
	
	@Controller
	public class IndexController {
		@RequestMapping("/")
	    public String index() {
	        return "redirect:/index";
	    }
	}
	
}
