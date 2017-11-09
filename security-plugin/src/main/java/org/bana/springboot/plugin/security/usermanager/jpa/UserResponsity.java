/**
 * 
 */
package org.bana.springboot.plugin.security.usermanager.jpa;

import org.bana.springboot.plugin.security.usermanager.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author liuwenjie
 *
 */
@Repository
public interface UserResponsity extends  PagingAndSortingRepository<UserEntity,Long>{

	Page<UserEntity> findByUsernameLike(@Param("username")String username,Pageable pageable);
	
	
	UserEntity findByUsername(@Param("username")String username);
	
	
    void deleteByUsername(@Param("username")String username);
}
