package org.bana.springboot.security.plugin;

import java.util.List;

import org.bana.springboot.security.plugin.error.BanaDefaultErrorAttributes;
import org.bana.springboot.security.plugin.error.BanaErrorController;
import org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.LoginController;
import org.bana.springboot.security.plugin.usermanager.RegisterController;
import org.bana.springboot.security.plugin.usermanager.UserManagerController;
import org.bana.springboot.security.plugin.usermanager.inmemory.CustomInMemoryUserDetailsManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

@Configuration
@EnableConfigurationProperties(BanaSecurityProperties.class)
@ConditionalOnProperty(prefix="bana.springboot.security",value="enabled",matchIfMissing=true)//可以是用bana.springboot.security.enabled=false屏蔽。matchIfMissing在没有配置时是否注入
@ConditionalOnWebApplication
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
@Import(UserManagerController.class)
public class BanaSecurityAutoConfiguration {
	
//	private static final Logger LOG = LoggerFactory.getLogger(BanaSecurityAutoConfiguration.class);
//	@Autowired
//	private BanaSecurityProperties banaSecurityProperties;
	
//	@Bean
//	public BanaExceptionHandlerAdvice defaultExceptionHandler(){
//		return new BanaExceptionHandlerAdvice();
//	}
//	@RestControllerAdvice
//	public class DefaultExceptionHandlerAdvice extends BanaExceptionHandlerAdvice{
//		
//	}
//	@Controller
//	@ConditionalOnMissingBean(BanaErrorController.class)
//	public class DefaultBanaErrorController extends BanaErrorController{
//		public DefaultBanaErrorController(ErrorAttributes errorAttributes,ServerProperties serverProperties,
//				ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
//			super(errorAttributes,serverProperties.getError(), errorViewResolversProvider.getIfAvailable());
//		}
//	}
	
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
	
	@Controller
	@ConditionalOnProperty(prefix="bana.springboot.security",value="defaultController",matchIfMissing=true)
	@ConditionalOnMissingBean(LoginController.class)
	public class DefaultLoginController extends LoginController{
		//通过自动注入方式把LoginController注入到系统中
	}
	
	@Controller
	@ConditionalOnProperty(prefix="bana.springboot.security",value="defaultRegisterController",matchIfMissing=true)
	@ConditionalOnMissingBean(RegisterController.class)
	public class DefaultRegisterController extends RegisterController{
		//自動注入一个默认的注册的Controller类
	}
	
//	@Controller
//	@ConditionalOnProperty(prefix="bana.springboot.security",value="defaultUserManagerController",matchIfMissing=true)//可以是用bana.springboot.security.enabled=false屏蔽。matchIfMissing在没有配置时是否注入
//	@ConditionalOnMissingBean(UserManagerController.class)
//	@ConditionalOnBean(CustomeUserDetailsManager.class)
//	public class DefaultUserManagerController extends UserManagerController{
//		//自動注入一个默认的UserManager的Controller类
//	}
	
	@Bean
	@ConditionalOnProperty(prefix="bana.springboot.security",value="defaultUserManagerController",matchIfMissing=true)//可以是用bana.springboot.security.enabled=false屏蔽。matchIfMissing在没有配置时是否注入
	@ConditionalOnMissingBean(UserManagerController.class)
	@ConditionalOnBean(CustomeUserDetailsManager.class)
	public UserManagerController defaultUserManagerController(){
		return new UserManagerController();
	}
	
	@Configuration
	@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
	@ConditionalOnMissingBean(BanaWebSecurityConfig.class)
	@ConditionalOnProperty(prefix="bana.springboot.security",value="defaultSecurityConfig",matchIfMissing=true)
	@Order(200)
	public class BanaWebSecurityConfigAutoConfig extends BanaWebSecurityConfig{
		
		//4
//	    @Override
//	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    	LOG.info("执行了默认的security安全配置，配置认证性质，使用什么内容");
//	    		auth
//	                .inMemoryAuthentication()
//	                .withUser("user").password("password").roles("USER")
//	                .and()
//	                .withUser("admin").password("admin").roles("admin");
//	    }
		
		@Bean
		@Override
		public CustomeUserDetailsManager userDetailsService() {
			CustomInMemoryUserDetailsManager manager = new CustomInMemoryUserDetailsManager();
			manager.createUser(User.withUsername("test").password("password").roles("USER").build());
			manager.createUser(User.withUsername("admin").password("admin").roles("admin").build());
			return manager;
		}
		
	}
	
}

