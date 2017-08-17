/**
 * 
 */
package org.bana.springboot.security.plugin.usermanager.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

/**
 * @author liuwenjie
 *
 */
@Entity
@Table(name = "t_bi_user")
public class UserEntity extends AbstractAuditingEntity {
	
	private static final long serialVersionUID = -916200490141212155L;
	
	@Id 
	@GeneratedValue
	private Long id;
	
	@Column(unique=true,nullable=false,length=191)
	private String username;
	
	private String password;
	
	private boolean enabled;
	
	@Column(nullable=true)
	private Date expireDateTime;
	
	private boolean accountLocked;
	
	@Column(nullable=true)
	private Date credentialsExpireDateTime;
	
	@JoinTable(name="t_ref_user_role",joinColumns={@JoinColumn(name="user_id",referencedColumnName="id",updatable=true,insertable=true)})
	@ElementCollection(targetClass=String.class,fetch=FetchType.EAGER)
	@Column(name="role_name")
	private List<String> roleList;
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getExpireDateTime() {
		return expireDateTime;
	}

	public void setExpireDateTime(Date expireDateTime) {
		this.expireDateTime = expireDateTime;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Date getCredentialsExpireDateTime() {
		return credentialsExpireDateTime;
	}

	public void setCredentialsExpireDateTime(Date credentialsExpireDateTime) {
		this.credentialsExpireDateTime = credentialsExpireDateTime;
	}
	

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled
				+ ", expireDateTime=" + expireDateTime + ", accountLocked=" + accountLocked
				+ ", credentialsExpireDateTime=" + credentialsExpireDateTime + "]";
	}
	
	

}
