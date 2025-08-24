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
	 * Search user by email
	 * @param email - unique count why identify {@link User}
	 * @return {@link User} if email contain in database, or Null if they not contain in data base
	 */
	public User findByEmail(String email) {
		if (email == null || !email.contains("@")) 
			return null;
		return users.findOneByEmail(email);
	}
	
	/**
	 * Method creation new user
	 * @param username - user name
	 * @param email - user email, necessary contain symbol @
	 * @param password - user password
	 * @return true if user save in database or false if data user not valid 
	 * @throws {@link RuntimeException} throws if email is busy
	 */
	public boolean New(String username, String email, String password) {
		if (username.isEmpty() || email.isEmpty() || password.isBlank() || !email.contains("@"))
			return false;
		if (findByEmail(email.toLowerCase()) != null)
			throw new RuntimeException("Email is Busy");
		
		User u = User.builder()
				.email(email.toLowerCase())
				.password(encode.encode(password))
				.username(username)
				.build();
		users.save(u);
		
		return true;
	}
	/**
	 * Method return all users object in database
	 * @return List {@link User}, IF USER not RULE ADMIN RETURN null
	 */
	public Collection<User> listAllUser(){
		if (getMe().getRole() != UserRole.ADMIN) return null;
		return users.findAll();
	}
	/**
	 * Method return user use id in database
	 * @param id - id user in database
	 * @return {@link User} get user by id, IF USER not RULE ADMIN RETURN null
	 */
	public User getUser(long id){
		if (getMe().getRole() != UserRole.ADMIN) return null;
		return users.findOneById(id);
	}
	
	/**
	 * Method delete user in database by id
	 * @param id - id user in database
	 * @return true can user deleted, IF USER not RULE ADMIN RETURN false
	 */
	public boolean delUser(long id) {
		if (getMe().getRole() != UserRole.ADMIN) return false;
		
		users.deleteById(id);
		return true;
	}
	
	/**
	 * Method generation jwt auth token
	 * @param email - app User email  
	 * @param password - app User password 
	 * @return jwt token to auth {@link User}, or null if user not found(or data user not correct)
	 */
	public String authTokenWithPassword(String email, String password) {
		if (email == null || password == null || !email.contains("@")) 
			return null;
	
		User usr = users.findOneByEmail(email.toLowerCase());
		
		if (usr != null && encode.matches(password, usr.getPassword())) {
			return jws.generateTokenForUser(usr);
		}
			
		return null;
	}
	
	/**
	 * Method transform user jwt token in {@link User.email}
	 * @param token - jwt auth token 
	 * @return {@link User.email}
	 * @throws {@link RuntimeException} throws if invalid token
	 */
	public String getEmailWithToken(String token) {
		if (jws.isValid(token))
			return jws.getEmailUserByToken(token);
		throw new RuntimeException("Invalid token");
	}
	
	
	/**
	 * Method transform user jwt token in User app
	 * @param token - jwt auth token 
	 * @return {@link User}
	 * @throws {@link RuntimeException} throws if invalid token
	 */
	public User getUserWithToken(String token) {
		if (jws.isValid(token))
			return findByEmail(getEmailWithToken(token));
		throw new RuntimeException("Invalid token");
	}
	
	/**
	 * Get user app from Spring security context holder
	 * @return Principal warper User app
	 */
	public User getMe() {
		return findByEmail(
				SecurityContextHolder.getContext().getAuthentication().getName()
				);
	}
	
}
