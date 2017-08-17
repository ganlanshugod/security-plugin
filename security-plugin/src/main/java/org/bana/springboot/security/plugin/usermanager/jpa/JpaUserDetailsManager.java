/**
 * 
 */
package org.bana.springboot.security.plugin.usermanager.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.domain.UserEntity;
import org.bana.springboot.security.plugin.usermanager.pojo.JpaUser;
import org.bana.springboot.security.plugin.usermanager.pojo.UserDetailQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.CollectionUtils;

/**
 * @author liuwenjie
 *
 */
public class JpaUserDetailsManager extends JdbcUserDetailsManager implements CustomeUserDetailsManager{

	private UserResponsity userResponsity;
	/* (non-Javadoc)
	 * @see org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager#findAll(org.bana.springboot.security.plugin.usermanager.pojo.UserDetailQuery, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<UserDetails> findAll(UserDetailQuery queryParam, Pageable pageable) {
		Page<UserEntity> result = null;
		if(StringUtils.isBlank(queryParam.getSearchText())){
			result = userResponsity.findAll(pageable);
		}else{
			result = userResponsity.findByUsernameLike(queryParam.getSearchText()+"%", pageable);
		}
		List<UserDetails> resultList = new ArrayList<UserDetails>();
		if(!CollectionUtils.isEmpty(result.getContent())){
			for (UserEntity userEntity : result.getContent()) {
				resultList.add(new JpaUser(userEntity));
			}
		}
		return new PageImpl<UserDetails>(resultList, pageable, result.getTotalElements());
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.provisioning.UserDetailsManager#createUser(org.springframework.security.core.userdetails.UserDetails)
	 */
	@Override
	public void createUser(UserDetails user) {
		userResponsity.save(parseUserDetail(user));
	}
	
	private UserEntity parseUserDetail(UserDetails user){
		if(user == null){
			throw new NullPointerException("userDetails 对象不能为null");
		}
		
		UserEntity userEntity = userResponsity.findByUsername(user.getUsername());
		if(userEntity == null){
			userEntity = new UserEntity();
			userEntity.setUsername(user.getUsername());
		}
		userEntity.setAccountLocked(!user.isAccountNonLocked());
		if(userEntity.getExpireDateTime() == null && !user.isAccountNonExpired()){
			userEntity.setExpireDateTime(new Date());
		}
		if(userEntity.getCredentialsExpireDateTime() == null && !user.isCredentialsNonExpired()){
			userEntity.setCredentialsExpireDateTime(new Date());
		}
		
		userEntity.setEnabled(user.isEnabled());
		userEntity.setPassword(user.getPassword());
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		List<String> roleList = userEntity.getRoleList();
		if(roleList == null){
			roleList = new ArrayList<String>();
			userEntity.setRoleList(roleList);
		}
		if(!CollectionUtils.isEmpty(authorities)){
			for (GrantedAuthority grantedAuthority : authorities) {
				if(!roleList.contains(grantedAuthority.getAuthority())){
					roleList.add(grantedAuthority.getAuthority());
				}
			}
			ArrayList<String> removeList = new ArrayList<String>();
			for (String string : roleList) {
				if(!authorities.contains(new SimpleGrantedAuthority(string))){
					removeList.add(string);
				}
			}
			roleList.removeAll(removeList);
		}else{
			roleList.clear();
		}
		return userEntity;
		
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.provisioning.UserDetailsManager#updateUser(org.springframework.security.core.userdetails.UserDetails)
	 */
	@Override
	@Transactional
	public void updateUser(UserDetails user) {
		userResponsity.save(parseUserDetail(user));
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.provisioning.UserDetailsManager#deleteUser(java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteUser(String username) {
		userResponsity.deleteByUsername(username);
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.provisioning.UserDetailsManager#changePassword(java.lang.String, java.lang.String)
	 */
	@Override
	public void changePassword(String oldPassword, String newPassword) {
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.provisioning.UserDetailsManager#userExists(java.lang.String)
	 */
	@Override
	public boolean userExists(String username) {
		return userResponsity.findByUsername(username) != null;
	}
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = userResponsity.findByUsername(username);
		if(userEntity == null){
			throw new UsernameNotFoundException(
					this.messages.getMessage("JdbcDaoImpl.notFound",
							new Object[] { username }, "Username {0} not found"));
		}
		return new JpaUser(userEntity);
	}
	public void setUserResponsity(UserResponsity userResponsity) {
		this.userResponsity = userResponsity;
	}
	
}
