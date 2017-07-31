package org.bana.springboot.security.plugin.usermanager;

import javax.validation.Valid;

import org.bana.springboot.security.plugin.BanaSecurityProperties;
import org.bana.springboot.security.plugin.usermanager.pojo.UserDetailRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class RegisterController {
	private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

	@Autowired
	protected BanaSecurityProperties banaSecurityProperties;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String toRegister() {
		LOG.info("使用默认装载的RegisterController类进行注册操作");
		return banaSecurityProperties.getRegisterView();
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@Valid UserDetailRegister userRegister, BindingResult bindingResult) {
		LOG.info("使用默认装载的RegisterController类进行注册保存操作");
		if (!bindingResult.hasErrors()) {
			if (userRegister.getPassword().equals(userRegister.getRepeatPassword())) {
				bindingResult.rejectValue("repeatPassword", "两次密码输入不一致");
			}
		}
		return banaSecurityProperties.getRegisterView();
	}

}