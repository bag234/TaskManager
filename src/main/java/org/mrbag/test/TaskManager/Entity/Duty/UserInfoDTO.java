package org.mrbag.test.TaskManager.Entity.Duty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

	@NotNull
	String username;
	
	@NotNull
	@Email
	String email;
	
	@NotNull
	String password;
	
}
