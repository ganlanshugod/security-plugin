/**
 * 
 */
package org.bana.springboot.plugin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author liuwenjie
 *
 */
public class BanaWebSecurityConfigurer extends AbstractHttpConfigurer<BanaWebSecurityConfigurer, HttpSecurity> {

	private static final Logger LOG = LoggerFactory.getLogger(BanaWebSecurityConfigurer.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.SecurityConfigurerAdapter#init(org.springframework.security.config.annotation.SecurityBuilder)
	 */
	@Override
	public void init(HttpSecurity http) throws Exception {
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
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.SecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.SecurityBuilder)
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
	}
	
	public static BanaWebSecurityConfigurer config() {
        return new BanaWebSecurityConfigurer();
    }
}
