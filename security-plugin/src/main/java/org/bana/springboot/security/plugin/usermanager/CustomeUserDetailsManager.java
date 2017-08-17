package org.bana.springboot.security.plugin.usermanager;

import org.bana.springboot.security.plugin.usermanager.pojo.UserDetailQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface CustomeUserDetailsManager extends UserDetailsManager{

	public Page<UserDetails> findAll(UserDetailQuery queryParam,Pageable pageable);
}
