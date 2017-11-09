package org.bana.springboot.plugin.security.usermanager.pojo;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class UserDetailAdd implements Serializable{

	private static final long serialVersionUID = -4939427277783072L;

	@NotBlank(message = "名称不能为空")
    @Size(min=4, max=30, message = "名字长度必须在5和30之间")
	private String username;
	@NotBlank(message = "密码不能为空")
    @Size(min=6, max=30, message = "名字长度必须在5和30之间")
	private String password;
	
	private String repeatPassword;
	
	private boolean isAdmin;

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

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	@Override
	public String toString() {
		return "UserDetailAdd [username=" + username + ", password=" + password + ", repeatPassword=" + repeatPassword
				+ ", isAdmin=" + isAdmin + "]";
	}
	
}
