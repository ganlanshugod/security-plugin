/**
 * 
 */
package org.bana.springboot.plugin.security.usermanager.pojo;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author liuwenjie
 *
 */
public class UserDetailUpdate implements Serializable{

	private static final long serialVersionUID = 7814354508898153320L;

	@NotBlank(message = "名称不能为空")
    @Size(min=4, max=30, message = "名字长度必须在5和30之间")
	private String username;
	
	private boolean enabled;
	
	private boolean admin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@Override
	public String toString() {
		return "UserDetailUpdate [username=" + username + ", enabled=" + enabled + ", admin=" + admin + "]";
	}
	
}
