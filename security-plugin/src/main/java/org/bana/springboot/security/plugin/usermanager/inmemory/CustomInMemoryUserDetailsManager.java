package org.bana.springboot.security.plugin.usermanager.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager;
import org.bana.springboot.security.plugin.usermanager.pojo.UserDetailQuery;
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
	 * @see org.bana.springboot.security.plugin.usermanager.CustomeUserDetailsManager#findAll(org.bana.springboot.security.plugin.usermanager.pojo.UserDetailQuery, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<UserDetails> findAll(UserDetailQuery queryParam, Pageable pageable) {
		Collection<UserDetails> values = userForSelect.values();
		Iterator<UserDetails> iterator = values.iterator();
		String searchText = queryParam.getSearchText();
		int begin = pageable.getOffset();
		int pageSize = pageable.getPageSize();
		List<UserDetails> resultList = new ArrayList<UserDetails>();
		while(iterator.hasNext()){
			UserDetails next = iterator.next();
			
			if(StringUtils.isBlank(searchText) || next.getUsername().contains(searchText)){
				resultList.add(next);
			}
		}
		return new PageImpl<UserDetails>(resultList.subList(begin, NumberUtils.min(new int[]{resultList.size(), pageSize})), pageable, resultList.size());
	}
	
}
