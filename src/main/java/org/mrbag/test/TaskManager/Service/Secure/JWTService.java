package org.mrbag.test.TaskManager.Service.Secure;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private final long expiration;
	
	private final SecretKey key;
	
	public JWTService(
			@Value("${app.exp}") long expiration, 
			@Value("${app.jwt}") String jwtKey
			) {
		this.expiration = expiration;
		this.key = Keys.hmacShaKeyFor(jwtKey.getBytes());
	}
	
	private static Map<String, Object> getClaims(User user){
		return Map.of(
				"Username", user.getUsername(),
				"Role", user.getRole().name(),
				"Password", "OFF"
				);
	}
	
	public String generateTokenForUser(User user) {
		return Jwts.builder()
				.claims(getClaims(user))
				.subject(user.getEmail())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(key)
				.compact();
	}
	
	private Claims getClaims(String token) {
		return (Claims) Jwts.parser().verifyWith(key).build().parse(token).getPayload();
	}
	
	public boolean isValid(String token) {
		if (token == null || token.isEmpty()) return false;
		try {
			return getClaims(token).getExpiration().after(new Date());
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public String getEmailUserByToken(String token) {
		return getClaims(token).getSubject();
	}
	
	public UserRole getUserRoleByToken(String token) {
		return UserRole.valueOf((String) getClaims(token).get("Role"));
	}
	
	@Deprecated
	public String getPasswordByToken(String token) {
		return (String) getClaims(token).get("Password");
	}
	
	public User getUserByToken(String token) {
		Claims c = getClaims(token);
		
		return User.builder().email(c.getSubject())
							 .role(UserRole.valueOf((String)c.get("Role")))
							 .build();
	}
	
	public String getUsernameyToken(String token) {
		return (String) getClaims(token).get("Username");
	}
	
}
