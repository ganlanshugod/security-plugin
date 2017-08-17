package org.bana.springboot.security.plugin.usermanager.jpa;

import org.apache.commons.lang3.StringUtils;
import org.bana.springboot.security.plugin.usermanager.domain.UserEntity;
import org.bana.springboot.security.plugin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
	
	@Autowired
	private UserResponsity userResponsity;

    @Override
    public String getCurrentAuditor() {
        String userName = SecurityUtils.getCurrentUserLogin();
        if(StringUtils.isNotBlank(userName)){
        	UserEntity user = userResponsity.findByUsername(userName);
        	if(user != null){
        		return user.getId()+"";
        	}
        }
        return "-1";
    }
}
