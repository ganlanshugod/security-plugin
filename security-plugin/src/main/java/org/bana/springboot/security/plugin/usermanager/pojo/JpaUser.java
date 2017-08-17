/**
 * 
 */
package org.bana.springboot.security.plugin.usermanager.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bana.springboot.security.plugin.usermanager.domain.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

/**
 * @author liuwenjie
 *
 */
public class JpaUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310369247094488670L;
	
	private UserEntity userEntity;
	
	private boolean entityChange = false;
	
	private List<SimpleGrantedAuthority> authorites;
	
	/**
	 * 
	 */
	public JpaUser() {
	}
	

	public JpaUser(UserEntity userEntity) {
		super();
		this.entityChange = true;
		this.userEntity = userEntity;
	}
	
	public UserEntity getUserEntity() {
		return userEntity;
	}


	public void setUserEntity(UserEntity userEntity) {
		this.entityChange = true;
		this.userEntity = userEntity;
	}


	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(authorites == null || this.entityChange){
			authorites = new ArrayList<SimpleGrantedAuthority>();
			List<String> roleList = this.userEntity.getRoleList();
			if(!CollectionUtils.isEmpty(roleList)){
				for (String string : roleList) {
					authorites.add(new SimpleGrantedAuthority(string));
				}
			}
			this.entityChange = false;
		}
		return authorites;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return this.userEntity.getPassword();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return this.userEntity.getUsername();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.userEntity.getExpireDateTime() == null || new Date().before(this.userEntity.getExpireDateTime());
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !this.userEntity.isAccountLocked();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return this.userEntity.getCredentialsExpireDateTime() == null || new Date().before(this.userEntity.getCredentialsExpireDateTime());
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return this.userEntity.isEnabled();
	}

}
