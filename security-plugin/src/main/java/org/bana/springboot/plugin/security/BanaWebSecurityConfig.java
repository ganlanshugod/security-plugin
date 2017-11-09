package org.bana.springboot.plugin.security;

import org.bana.common.util.basic.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BanaWebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final Logger LOG = LoggerFactory.getLogger(BanaWebSecurityConfig.class);
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	LOG.info("执行了Bana默认的WebSecurityConfigurerAdapter的http配置");
    	http.apply(BanaWebSecurityConfigurer.config());
    }

    //5忽略静态资源的拦截
    @Override
    public final void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/webjars/**","/assets/**","/favicon.ico");
        this.banaConfigure(web);
    }
    
    //可以通过子类来增加配置和重载对应的配置方法
    protected void banaConfigure(WebSecurity web) throws Exception{
    }
    
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    	LOG.info("执行了默认的security安全配置，配置认证性质，使用什么内容");
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
    
}