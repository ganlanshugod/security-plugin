package org.bana.springboot.plugin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="bana.springboot.security")
public class BanaSecurityProperties {
	
	private static final String DEFAULT_LOGIN_VIEW = "bana-security/login/boot-form-login1";
	
	private static final String DEFAULT_REGISTER_VIEW = "bana-security/login/boot-form-register1";

	/**
	 * 登录页面的页面路径
	 */
	private String loginView = DEFAULT_LOGIN_VIEW;
	
	private String registerView = DEFAULT_REGISTER_VIEW;
	
	private String configType;
	
	
	public String getRegisterView() {
		return registerView;
	}

	public void setRegisterView(String registerView) {
		this.registerView = registerView;
	}

	public String getLoginView() {
		return loginView;
	}

	public void setLoginView(String loginView) {
		this.loginView = loginView;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

}
