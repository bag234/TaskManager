package org.mrbag.test.TaskManager.Controller;

import java.util.Collection;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService users;
	
	@Tag(name = "ALL", description = " Get-методы для пользователей")
	@ApiResponse(responseCode = "200", description = "Возвращает экземпляр пользователя сохраненного в контексте приложения")
	@GetMapping("/me")
	public User getMe() {
		return users.getMe();
	}
	
	@Tag(name = "ADMIN", description = "Административные методы")
	@ApiResponse(responseCode = "200", description = "Возвращает список пользователей")
	@GetMapping
	public Collection<User> getAllUser(){
		return users.listAllUser();
	}

	@Tag(name = "ADMIN", description = "Административные методы")
	@ApiResponse(responseCode = "200", description = "Получает экземпляр пользователя по ID")
	@GetMapping("/{id}")
	public User getUserById(@PathVariable("id") long id ) {
		return users.getUser(id);
	}
	
	@Tag(name = "ADMIN", description = "Административные методы")
	@ApiResponse(responseCode = "200", description = "Удалят экземпляр пользователя по ID")
	@DeleteMapping("/{id}")
	public boolean deleteUserById(@PathVariable("id") long id ) {
		return users.delUser(id);
	}
	
}
