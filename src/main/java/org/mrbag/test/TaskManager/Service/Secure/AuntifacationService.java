package org.mrbag.test.TaskManager.Service.Secure;

import java.util.List;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Repository.UserRep;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuntifacationService {
	// https://habr.com/ru/articles/784508/ https://habr.com/ru/articles/938518/
	@Autowired
	JWTService jws;
	
	@Autowired
	UserRep users;
	
	
	
	public Authentication singInWithToken(String token) {
		if (!jws.isValid(token)) return null;
		
		User usr = jws.getUserByToken(token);
		if (usr == null) return null;
		
		return new UsernamePasswordAuthenticationToken(
				usr.getEmail(), 
				null, 
				List.of(
						new SimpleGrantedAuthority(usr.getRole().name())
						));
		
	}
}
