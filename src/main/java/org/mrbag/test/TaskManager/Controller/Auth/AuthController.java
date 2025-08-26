package org.mrbag.test.TaskManager.Controller.Auth;

import org.mrbag.test.TaskManager.Entity.Duty.UserInfoDTO;
import org.mrbag.test.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserService users;
	
	@PostMapping("/register")
	@ApiResponse(responseCode = "200", description = "Возращает успех регистрации нового пользоваетля")
	public ResponseEntity<Boolean> registerNewUser(
			@RequestBody @Valid UserInfoDTO user
			){
		return ResponseEntity.ok(users.New(user.getUsername(), user.getEmail(), user.getPassword()));
	}
	
	@PostMapping("/login")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Возращает jwt токен для авториазации"),
		@ApiResponse(responseCode = "405", description = "Если пользователя нет в базе данных")
	})
	public ResponseEntity<JWTResponseDTO> loginUser(
			@RequestBody @Valid LoginRequstDTO user
			){
		String token = users.authTokenWithPassword(user.getEmail(), user.getPassword());
		if (token != null)
			return ResponseEntity.ok(new JWTResponseDTO(token));
	
		return ResponseEntity.status(405).build();
	}
	
	@AllArgsConstructor
	@Getter
	@Schema(description = "Экземпля обертки для jwt токена")
	static public class JWTResponseDTO{
		String token;
	}
	
	@Data
	@Schema(description = "Обертка для данных для авториазции")
	static public class LoginRequstDTO{
		@NotNull
		@Email
		String email;
		@NotNull
		String password;
	}
}
