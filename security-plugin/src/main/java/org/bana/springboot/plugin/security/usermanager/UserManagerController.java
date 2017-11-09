package org.bana.springboot.plugin.security.usermanager;

import java.util.HashSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.bana.springboot.plugin.RestResponseResult;
import org.bana.springboot.plugin.security.usermanager.pojo.JpaUser;
import org.bana.springboot.plugin.security.usermanager.pojo.UserDetailAdd;
import org.bana.springboot.plugin.security.usermanager.pojo.UserDetailQuery;
import org.bana.springboot.plugin.security.usermanager.pojo.UserDetailUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Validated
public class UserManagerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserManagerController.class);
	
	@Autowired
	protected CustomeUserDetailsManager userDetailsManager;

	@RequestMapping("/manager")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String toUserManger(Model model){
		return "bana-security/user/userManager";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Page<UserDetails> userList(UserDetailQuery userQuery ,@PageableDefault Pageable pageable){
		LOG.info("分页查询用户数据" + pageable.getOffset() + ":" + pageable.getPageNumber() + ";" + pageable.getPageSize());
//		new PageRequest(request);
		Page<UserDetails> findAll = userDetailsManager.findAll(userQuery,pageable);
//		Page<UserDetails> list = new PageImpl<UserDetails>();
		return findAll;
	}
	
	@RequestMapping("/userExists")
	@ResponseBody
	public RestResponseResult userExists(@NotNull(message = "用户名不能为空") String username){
//		System.out.println(bindResult.hasErrors());
		System.out.println("进入控制器");
		return new RestResponseResult(userDetailsManager.userExists(username));
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public RestResponseResult deleteUser(@NotNull(message="必须指定删除的用户名") String username){
		LOG.info("执行删除用户" + username);
		userDetailsManager.deleteUser(username);
		return new RestResponseResult();
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public RestResponseResult updateUser(@Valid UserDetailUpdate updateUser){
		LOG.info("执行用户修改流程");
		UserDetails user = userDetailsManager.loadUserByUsername(updateUser.getUsername());
		if(user == null){
			return new RestResponseResult("301","不存在的用户" + updateUser.getUsername());
		}
		if(user instanceof User || user instanceof JpaUser){
			HashSet<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(user.getAuthorities());
			if(updateUser.isAdmin()){
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}else{
				authorities.remove(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
			userDetailsManager.updateUser(new User(user.getUsername(), user.getPassword(), updateUser.isEnabled(), user.isAccountNonExpired(),
					user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities));
			return new RestResponseResult();
		}else{
			System.out.println(user.getClass().getName());
			return new RestResponseResult("302","不支持的UserDetails类型" + user.getClass().getName() + "用户名为" +  updateUser.getUsername());
		}
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public RestResponseResult addUser(@Valid final UserDetailAdd addUser,BindingResult bindResult) throws BindException{
		LOG.info("正常执行了添加用户的方法");
		if(bindResult.hasErrors()){
			throw new BindException(bindResult);
		}else{
			if(!addUser.getPassword().equals(addUser.getRepeatPassword())){
				bindResult.rejectValue("repeatPassword", "notEqual", "重复密码与原密码不一致");
				throw new BindException(bindResult);
			}
			if(userDetailsManager.userExists(addUser.getUsername())){
				bindResult.rejectValue("username", "missFormat","用户名已存在");
				throw new BindException(bindResult);
			}
		}
		
		UserBuilder userBulider = User.withUsername(addUser.getUsername()).password(addUser.getPassword()).roles("USER");
		if(addUser.isAdmin()){
			userBulider.roles("ADMIN");
		}
		userDetailsManager.createUser(userBulider.build());
//		if(bindResult.hasErrors()){
//			LOG.info("存在验证错误，无法执行,错误的数量为"+bindResult.getFieldErrorCount());
//		}
		return new RestResponseResult("success");
	}
}
