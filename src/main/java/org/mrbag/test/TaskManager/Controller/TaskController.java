package org.mrbag.test.TaskManager.Controller;

import java.util.Collection;
import java.util.List;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.mrbag.test.TaskManager.Entity.Duty.TaskDTO;
import org.mrbag.test.TaskManager.Service.TaskService;
import org.mrbag.test.TaskManager.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	@Autowired
	TaskService tasks;
	
	@Autowired
	UserService service;
	
	@PostMapping("")
	public ResponseEntity<Task> createTask(
			@RequestBody TaskDTO newTask
			) {
		Task t = tasks.newTask(newTask.toTask(service.getMe()));
		if (t == null)
			return ResponseEntity.status(400).build();
		return ResponseEntity.status(201).body(t);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTask(
			@PathVariable(name = "id") String id
			){
		Task t = tasks.getTask(Long.parseLong(id), service.getMe());
		if (t == null)
			return ResponseEntity.status(400).build();
		return ResponseEntity.status(201).body(t);
	}
	
	@GetMapping 
	public ResponseEntity<Collection<Task>> getListTasks(
			@RequestParam(name = "status", required = false) List<TaskStatus> status, 
			@RequestParam(name = "priority", required = false) List<TaskPriority> prioryti,
			@RequestParam(name = "assigne", required = false) List<Long> assid,
			@RequestParam(name = "author", required = false) List<Long> autid
			){
		
		return ResponseEntity.ok(tasks.getByFilter(status, prioryti, assid, autid, service.getMe()));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTask(
			@PathVariable(name = "id") String id,
			@RequestBody TaskDTO newTask
			) {
		Task t = newTask.toTask(service.getMe());
		t.setId(Long.parseLong(id));
		
		if (tasks.updateTask(t))
			return ResponseEntity.status(200).build();
		return ResponseEntity.status(400).build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delTask(
			@PathVariable(name = "id") String id
			){
		if (tasks.deleteTask(Long.parseLong(id), service.getMe()))
			return  ResponseEntity.status(200).build();
		return ResponseEntity.status(400).build();
	}
	
}
