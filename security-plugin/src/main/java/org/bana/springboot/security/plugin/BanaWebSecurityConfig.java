package org.bana.springboot.security.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class BanaWebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final Logger LOG = LoggerFactory.getLogger(BanaWebSecurityConfig.class);
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	LOG.info("执行了默认的security安全配置，配置登录对应的设置");
        http
                .authorizeRequests()
                .antMatchers("/login","/register").permitAll()//1根路径和/login路径不拦截
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login") //2登陆页面，决定是否走DefaultLoginPageGeneratingFilter 的配置项
                .defaultSuccessUrl("/") //3登陆成功转向该页面
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and().rememberMe().key("bana-security").tokenValiditySeconds(14*24*60*60);
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
    
}