package org.mrbag.test.TaskManager.Service;

import java.util.Collection;
import java.util.List;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.mrbag.test.TaskManager.Repository.TaskRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
/**
 * Simple implementation for work with app database 
 */
public class TaskService {
	
	@Autowired
	TaskRep tasks;

	/**
	 * Creation new task and save in data Base
	 * @param t -{@link Task} object who contain info about task
	 * @return {@link Task} if task save in database or null if task not correct
	 */
	public Task newTask(Task t) {
		if (t == null ||t.getAuthor() == null || t.getTitle() == null || t.getTitle().isEmpty())
				return null;
		
		return tasks.save(t);
	}
	
	/**
	 * Return task if user create this task or user has admin
	 * @param id - id task in database
	 * @param usr - Principal warper in context app
	 * @return Task or null if data not correct
	 */
	public Task getTask(Long id, User usr) {
		if (usr == null) return null;
		return tasks.findOnebyIdAnUser(id, usr, usr.getRole() == UserRole.ADMIN);
	}
	
	/**
	 * Update Task from TaskDTO
	 * @param t - Task warper from TaskDTO
	 * @return succefull update 
	 */
	@Transactional
	public boolean updateTask(Task t) {
		if (t == null) return false;
		
		if (tasks.updateTask(t, t.getAuthor().getRole() == UserRole.ADMIN) > 0) {
			if (t.getAssigner() != null) 
				tasks.updateTaskAssigner(t.getId(), t.getAssigner().getId());
			return true;
		}
		
		return false;
	}
	
	/**
	 * Delete Task from data base </br>
	 * Task must delete author or user has role Admin
	 * @param id - id task in database
	 * @param usr - User who delete database
	 * @return succefull update
	 */
	@Transactional
	public boolean deleteTask(Long id, User usr) {
		return tasks.dropTask(id, usr, usr.getRole() == UserRole.ADMIN) > 0;
	}
	
	/**
	 * Get all Task user, and if call user has role Admin all task in database, by filter
	 * @param status - List {@link TaskStatus}
	 * @param prioryti - List {@link TaskPriority}
	 * @param assid - List Author id 
	 * @param autid - List assigner id
	 * @param usr - Principal warper in context app
	 * @return List tasks
	 */
	public Collection<Task> getByFilter(
			List<TaskStatus> status, 
			List<TaskPriority> prioryti, 
			List<Long> assid, 
			List<Long> autid, 
			User usr){
		return tasks.getByFilter(status, prioryti, assid, autid, usr, usr.getRole() == UserRole.ADMIN);
	}
	
}
