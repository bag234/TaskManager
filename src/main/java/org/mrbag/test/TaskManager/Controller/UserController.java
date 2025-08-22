package org.mrbag.test.TaskManager.Controller;

import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService users;
	
	@GetMapping("/me")
	public User getMe() {
		return users.getMe();
	}
	
	// TODO Add implementation end point
	
}
