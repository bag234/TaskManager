package org.mrbag.test.TaskManager.Mockito;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.mrbag.test.TaskManager.Repository.TaskRep;
import org.mrbag.test.TaskManager.Service.TaskService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Task Service tester")
public class TestServiceMockito {

	final static int COUNT_TASK = 10; 
	
	final User usr = User.builder()
			.password("test")
			.username("test")
			.email("password")
			.role(UserRole.ADMIN)
			.build();
	
	@Mock
	TaskRep tasks;
	
	TaskService servTasks;
	
	static List<Task> tks;
	
	private void configurationTaskRep() {
		tks = new ArrayList<Task>();
		doReturn(-1).when(tasks).updateTask(any(), usr.getRole() == UserRole.ADMIN);
		for (int i = 0; i < COUNT_TASK; i++) {
			tks.add(
					Task.builder()
						.id(i)
						.title("mockito ~" + i)
						.description("test mockito~"+i)
						.author(usr)
						.createdAt(LocalDateTime.now())
						.updateAt(LocalDateTime.now())
						.build()
					);
			doReturn(tks.get(i)).when(tasks).findOnebyIdAnUser(i, usr, usr.getRole() == UserRole.ADMIN);
			doReturn(1).when(tasks).updateTask(tks.get(i), usr.getRole() == UserRole.ADMIN);
			doReturn(1).when(tasks).dropTask(i, usr, usr.getRole() == UserRole.ADMIN);
		}
		doReturn(tks.getFirst()).when(tasks).save(any());
		
		doReturn(-1).when(tasks).dropTask(-1, usr, usr.getRole() == UserRole.ADMIN);
//		doReturn(tasks)
	}
	
	@BeforeAll
	public void preLoadContext() {
		configurationTaskRep();
		servTasks = new TaskService();
		servTasks.setTasks(tasks);
	}
	
	@Test
	@DisplayName("Test save")
	public void testSave() {
		//Check valid logic 
		assertEquals(servTasks.newTask(tks.getFirst()), tks.getFirst());
		//Checked rule filter not correct date
		assertNull(servTasks.newTask(null));
		assertNull(servTasks.newTask(new Task(-1, null, "Test desc", null, null, usr, null, null, null)));
		assertNull(servTasks.newTask(new Task(-1, "", "Test desc", null, null, usr, null, null, null)));
		assertNull(servTasks.newTask(new Task(-1, "Test", "Test desc", null, null, null, null, null, null)));
	}
	
	@Test
	@DisplayName("Test get by id")
	public void testGetTask() {
		for (int i = 0; i< COUNT_TASK; i++) {
			assertEquals(tks.get(i), servTasks.getTask((long) i, usr));
		}
	}
	
}
