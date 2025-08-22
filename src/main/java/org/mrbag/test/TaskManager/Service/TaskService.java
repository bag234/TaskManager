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
public class TaskService {

	TaskRep tasks;
	
	@Autowired
	public void setTasks(TaskRep tasks) {
		this.tasks = tasks;
	}
	
	public Task newTask(Task t) {
		if (t == null ||t.getAuthor() == null || t.getTitle() == null || t.getTitle().isEmpty())
				return null;
		
		return tasks.save(t);
	}
	
	public Task getTask(Long id, User usr) {
		if (usr != null) return null;
		return tasks.findOnebyIdAnUser(id, usr, usr.getRole() == UserRole.ADMIN);
	}
	
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
	
	@Transactional
	public boolean deleteTask(Long id, User usr) {
		int c = tasks.dropTask(id, usr, usr.getRole() == UserRole.ADMIN);
		return c > 0;
	}
	
	public Collection<Task> getByFilter(List<TaskStatus> status, List<TaskPriority> prioryti, List<Long> assid, List<Long> autid, User usr){
		return tasks.getByFilter(status, prioryti, assid, autid, usr, usr.getRole() == UserRole.ADMIN);
	}
	
}
