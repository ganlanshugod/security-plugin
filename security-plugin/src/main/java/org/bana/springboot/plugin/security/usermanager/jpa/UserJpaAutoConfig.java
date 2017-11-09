/**
 * 
 */
package org.bana.springboot.plugin.security.usermanager.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author liuwenjie
 *
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan("org.bana.springboot.plugin.security.usermanager.domain")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class UserJpaAutoConfig {

	
}
