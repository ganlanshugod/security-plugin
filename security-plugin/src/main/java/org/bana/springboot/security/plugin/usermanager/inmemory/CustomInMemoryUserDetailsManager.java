package org.bana.springboot.security.plugin.usermanager.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
/**
 * 自定义一个内存处理的UserDetailsManger，来处理用户管理的其他方法
 * @author liuwenjie
 *
 */
public class CustomInMemoryUserDetailsManager extends InMemoryUserDetailsManager implements CustomeUserDetailsManager{

	private final Map<String, UserDetails> userForSelect = new HashMap<String, UserDetails>();
	
	@Override
	public void createUser(UserDetails user) {
		super.createUser(user);
		userForSelect.put(user.getUsername(), user);
	}
	
	@Override
	public void updateUser(UserDetails user) {
		super.updateUser(user);
		userForSelect.put(user.getUsername(), user);
	}
	
	@Override
	public void deleteUser(String username) {
		super.deleteUser(username);
		userForSelect.remove(username);
	}

	public Collection<UserDetails> findAll() {
		return userForSelect.values();
	}

	/* (non-Javadoc)
	 * @see org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager#findAll(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<UserDetails> findAll(Pageable pageable) {
		Collection<UserDetails> values = userForSelect.values();
		Iterator<UserDetails> iterator = values.iterator();
		int begin = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		int index = 0;
		List<UserDetails> resultList = new ArrayList<UserDetails>();
		while(iterator.hasNext()){
			UserDetails next = iterator.next();
			if(begin <= index){
				resultList.add(next);
			}
			index ++;
			if(resultList.size() >= pageSize){
				break;
			}
		}
		return new PageImpl<UserDetails>(resultList, pageable, values.size());
	}
	
}
