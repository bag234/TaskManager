package org.mrbag.test.TaskManager.Service.JWT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.mrbag.test.TaskManager.Service.Secure.JWTService;

public class TestServiceJWT {

	private static JWTService jws; 
	
	private static User usr;
	
	@BeforeAll
	static void configureJWT() {
		jws = new JWTService(1000, "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855");
		usr = new User(-1, "test", "test@test", "test", UserRole.ADMIN);
	}
	
	@Test
	@DisplayName("Convirtation")
	public void testAuthToken() {
		String tkn = jws.generateTokenForUser(usr);
	
		assertEquals(jws.getEmailUserByToken(tkn), usr.getEmail(), "Email convertion token not equlas");
		assertEquals(jws.getUsernameyToken(tkn), usr.getUsername(), "Username not eqlas in token");
		assertEquals(jws.getUserRoleByToken(tkn), usr.getRole(), "Role not Equlas");
		
		assertTrue(jws.isValid(tkn), "Wrong validation token");
	}
	
	@Test 
	@DisplayName("Time&Signature test token")
	public void testTimeBoarderJWT() throws Exception {
		JWTService jws_low = new JWTService(10, "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327856");
		String l_tkn = jws_low.generateTokenForUser(usr);
		Thread.sleep(20);
		
		assertFalse(jws_low.isValid(l_tkn), "Wrong work function is valid");
		assertFalse(jws.isValid(l_tkn), "Wrong signature algorithm");
	}
	
}
