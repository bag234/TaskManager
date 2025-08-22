package org.mrbag.test.TaskManager.Controller.Auth;

import org.mrbag.test.TaskManager.Entity.Duty.UserInfoDTO;
import org.mrbag.test.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserService users;
	
	@PostMapping("/register")
	public ResponseEntity<Boolean> registerNewUser(
			@RequestBody UserInfoDTO user
			){
		return ResponseEntity.ok(users.New(user.getUsername(), user.getEmail(), user.getPassword()));
	}
	
	@PostMapping("/login")
	public ResponseEntity<JWTResponseDTO> loginUser(
			@RequestBody LoginRequstDTO user
			){
		String token = users.authTokenWithPassword(user.getEmail(), user.getPassword());
		if (token != null)
			return ResponseEntity.ok(new JWTResponseDTO(token));
	
		return ResponseEntity.status(405).build();
	}
	
	@AllArgsConstructor
	@Getter
	static public class JWTResponseDTO{
		String token;
	}
	
	@Data
	static public class LoginRequstDTO{
		String email;
		String password;
	}
}
