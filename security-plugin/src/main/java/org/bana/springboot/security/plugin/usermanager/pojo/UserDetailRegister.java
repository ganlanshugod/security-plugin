package org.bana.springboot.security.plugin.usermanager.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 用户注册的实体类对象
 * @author liuwenjie
 *
 */
public class UserDetailRegister implements Serializable{

	private static final long serialVersionUID = 2174445333070269494L;
	
	@NotNull(message = "名称不能为空")
    @Size(min=4, max=30, message = "名字长度必须在5和30之间")
	private String username;
	@NotNull(message = "密码不能为空")
    @Size(min=6, max=30, message = "名字长度必须在5和30之间")
	private String password;
	
	private String repeatPassword;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRepeatPassword() {
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}
	
}
