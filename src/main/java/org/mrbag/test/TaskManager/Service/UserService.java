package org.mrbag.test.TaskManager.Service;

import java.util.Collection;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.mrbag.test.TaskManager.Repository.UserRep;
import org.mrbag.test.TaskManager.Service.Secure.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
/***
 * additional layer business logic who controlling right data and rules
 */
public class UserService {

	
	@Autowired
	UserRep users;
	
	@Autowired
	JWTService jws;
	
	@Autowired
	PasswordEncoder encode;
	
	/***
	 * Метод реализует дополнительную логику фильтрации что сократить потоциальное количестов обращанией к базе данных
	 * @param email - unique count why identify {@link User}
	 * @return {@link User} if email contain in database, or Null if they not contain in data base
	 */
	public User findByEmail(String email) {
		if (email == null || !email.contains("@")) 
			return null;
		return users.findOneByEmail(email);
	}
	
	
	public boolean New(String username, String email, String password) {
		if (username.isEmpty() || email.isEmpty() || password.isBlank() || !email.contains("@"))
			return false;
		if (findByEmail(email.toLowerCase()) != null)
			throw new RuntimeException("Email is Bussy");
		
		
		User u = User.builder()
				.email(email.toLowerCase())
				.password(encode.encode(password))
				.username(username)
				.build();
		users.save(u);
		
		return true;
	}
	
	public Collection<User> listAllUser(){
		if (getMe().getRole() != UserRole.ADMIN) return null;
		return users.findAll();
	}
	
	public User getUser(long id){
		if (getMe().getRole() != UserRole.ADMIN) return null;
		return users.findOneById(id);
	}
	
	public boolean delUser(long id) {
		if (getMe().getRole() != UserRole.ADMIN) return false;
		
		users.deleteById(id);
		return true;
	}
	
	public String authTokenWithPassword(String email, String password) {
		if (email == null || password == null || !email.contains("@")) 
			return null;
	
		User usr = users.findOneByEmail(email.toLowerCase());
		
		if (usr != null && encode.matches(password, usr.getPassword())) {
			return jws.generateTokenForUser(usr);
		}
			
		return null;
	}
	
	public String getEmailWithToken(String token) {
		if (jws.isValid(token))
			return jws.getEmailUserByToken(token);
		throw new RuntimeException("Invalid token");
	}
	
	public User getUserWithToken(String token) {
		return findByEmail(getEmailWithToken(token));
	}
	
	public User getMe() {
		return findByEmail(
				SecurityContextHolder.getContext().getAuthentication().getName()
				);
	}
	
}
