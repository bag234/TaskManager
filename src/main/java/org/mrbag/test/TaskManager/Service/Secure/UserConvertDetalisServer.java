package org.mrbag.test.TaskManager.Service.Secure;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserConvertDetalisServer implements UserDetailsService {

	@Autowired
	UserRep users; 
	
	/**
	 * Так по заданию имя пользователя не уникально то я не буду использовать его для аунтификации
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User usr = users.findOneByEmail(username);
		if (usr == null)
			throw new UsernameNotFoundException("Wrong EMail");
		
		return usr;
	}

}
