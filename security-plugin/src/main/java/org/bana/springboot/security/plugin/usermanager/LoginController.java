package org.bana.springboot.security.plugin.usermanager;

import org.bana.springboot.security.plugin.BanaSecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

public class LoginController{
	
	@Autowired
	private BanaSecurityProperties banaSecurityProperties;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping("/login")
	public String login(){
		LOG.info("使用默认装载的LoginController类进行登录操作");
		return banaSecurityProperties.getLoginView();
	}
}