package org.mrbag.test.TaskManager.Controller;

import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service")
public class ServiceAPIController {

	@GetMapping("/status")
	public TaskStatus[] getAllPosibleStatus() {
		return TaskStatus.values();
	}
	
	@GetMapping("/priority")
	public TaskPriority[] getAllPosiblePriority() {
		return TaskPriority.values();
		
	}
	
}
