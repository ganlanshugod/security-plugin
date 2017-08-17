package org.bana.springboot.security.plugin;

import java.util.List;

import javax.sql.DataSource;

import org.bana.common.util.basic.MD5Util;
import org.bana.springboot.security.plugin.error.BanaDefaultErrorAttributes;
import org.bana.springboot.security.plugin.error.BanaErrorController;
import org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.LoginController;
import org.bana.springboot.security.plugin.usermanager.RegisterController;
import org.bana.springboot.security.plugin.usermanager.UserManagerController;
import org.bana.springboot.security.plugin.usermanager.inmemory.CustomInMemoryUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.jpa.JpaUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.jpa.UserJpaAutoConfig;
import org.bana.springboot.security.plugin.usermanager.jpa.UserResponsity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Configuration
@EnableConfigurationProperties(BanaSecurityProperties.class)
@ConditionalOnProperty(prefix="bana.springboot.security",value="enabled",matchIfMissing=true)//可以是用bana.springboot.security.enabled=false屏蔽。matchIfMissing在没有配置时是否注入
@ConditionalOnWebApplication
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class,DataSourceAutoConfiguration.class})
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
	@AutoConfigureAfter(DataSourceAutoConfiguration.class)
	@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
	@ConditionalOnMissingBean(value={DataSource.class})
	@ConditionalOnProperty(prefix="bana.springboot.security",value="configType",havingValue="inMemery",matchIfMissing=true)
	@Order(200)
	public class BanaWebSecurityConfigAutoConfig extends BanaWebSecurityConfig{
		
		@Bean
		@Override
		public CustomeUserDetailsManager userDetailsService() {
			CustomInMemoryUserDetailsManager manager = new CustomInMemoryUserDetailsManager();
			manager.createUser(User.withUsername("test").password("password").roles("USER").build());
			manager.createUser(User.withUsername("admin").password("admin").roles("USER","ADMIN").build());
			return manager;
		}
		
	}
	
	@Configuration
	@ConditionalOnClass({WebSecurityConfigurerAdapter.class})
	@ConditionalOnMissingBean(BanaWebSecurityConfig.class)
	@ConditionalOnBean(DataSource.class)
	@ConditionalOnProperty(prefix="bana.springboot.security",value="configType",havingValue="jdbc",matchIfMissing=true)
	@Order(200)
	@Import({UserJpaAutoConfig.class})
	@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
	public class BanaJdbcWebSecurityConfigAutoConfig extends BanaWebSecurityConfig{
		
		@Autowired
		private DataSource dataSource;
		@Autowired
		private UserResponsity userResponsity;
		//4
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    	LOG.info("执行了默认的security安全配置，配置认证性质，使用什么内容");
	    		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	    }
	    
	    
	    @Bean
	    public PasswordEncoder passwordEncoder(){
	    	return new PasswordEncoder() {
				@Override
				public boolean matches(CharSequence rawPassword, String encodedPassword) {
					return MD5Util.getMD5UseKey(rawPassword.toString()).equalsIgnoreCase(encodedPassword);
				}
				
				@Override
				public String encode(CharSequence rawPassword) {
					return MD5Util.getMD5UseKey(rawPassword.toString());
				}
			};
	    }
	    
	    @Bean
		@Override
		public CustomeUserDetailsManager userDetailsService() {
	    	JpaUserDetailsManager manager = new JpaUserDetailsManager();
	    	manager.setDataSource(dataSource);
	    	manager.setUserResponsity(userResponsity);
	    	if(!manager.userExists("user")){
	    		manager.createUser(User.withUsername("user").password(passwordEncoder().encode("password")).roles("USER").build());
	    	}
	    	if(!manager.userExists("admin")){
	    		manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("USER","ADMIN").build());
	    	}
			return manager;
		}
	}
	
	
	
}

