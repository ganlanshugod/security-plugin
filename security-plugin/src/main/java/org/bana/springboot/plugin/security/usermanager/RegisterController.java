package org.bana.springboot.plugin.security.usermanager;

import javax.validation.Valid;

import org.bana.springboot.plugin.RestResponseResult;
import org.bana.springboot.plugin.security.BanaSecurityProperties;
import org.bana.springboot.plugin.security.usermanager.pojo.UserDetailRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class RegisterController {
	private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private UserDetailsManager userDetailsManager;

	@Autowired
	protected BanaSecurityProperties banaSecurityProperties;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String toRegister() {
		LOG.info("使用默认装载的RegisterController类进行注册操作");
		return banaSecurityProperties.getRegisterView();
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public RestResponseResult register(@Valid UserDetailRegister userRegister, BindingResult bindingResult) throws BindException{
		LOG.info("使用默认装载的RegisterController类进行注册保存操作");
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		if (!userRegister.getPassword().equals(userRegister.getRepeatPassword())) {
			bindingResult.rejectValue("repeatPassword","notEqual", "两次密码输入不一致");
			throw new BindException(bindingResult);
		}
		if(userDetailsManager.userExists(userRegister.getUsername())){
			bindingResult.rejectValue("username", "missFormat","用户名已存在");
			throw new BindException(bindingResult);
		}
		
		UserBuilder userBulider = User.withUsername(userRegister.getUsername()).password(userRegister.getPassword()).roles("user");
		userDetailsManager.createUser(userBulider.build());
		
		return new RestResponseResult();
	}

}