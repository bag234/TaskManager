package org.mrbag.test.TaskManager.Mockito;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.User;
import org.mrbag.test.TaskManager.Entity.UserRole;
import org.mrbag.test.TaskManager.Repository.TaskRep;
import org.mrbag.test.TaskManager.Service.TaskService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Task Service tester")
public class TestServiceMockito {

	final static int COUNT_TASK = 10; 
	
	final static User usr = User.builder()
			.password("test")
			.username("test")
			.email("password")
			.role(UserRole.ADMIN)
			.build();
	
	@Mock
	TaskRep tasks;
	
	@InjectMocks
	TaskService servTasks;
	
	List<Task> tks;
	
	private void configurationTaskRep() {
		tks = new ArrayList<Task>();
		doReturn(-1).when(tasks).updateTask(any(), eq(usr.getRole() == UserRole.ADMIN));
		for (int i = 0; i < COUNT_TASK; i++) {
			Task t = Task.builder()
					.id(i)
					.title("mockito ~" + i)
					.description("test mockito~"+i)
					.author(usr)
					.createdAt(LocalDateTime.now())
					.updateAt(LocalDateTime.now())
					.build();
			tks.add(t);
			doReturn(t).when(tasks).findOnebyIdAnUser(eq((long) i), eq(usr), eq(usr.getRole() == UserRole.ADMIN));
			doReturn(1).when(tasks).updateTask(eq(tks.get(i)), eq(usr.getRole() == UserRole.ADMIN));
			doReturn(1).when(tasks).dropTask(eq((long) i), eq(usr), eq(usr.getRole() == UserRole.ADMIN));
		}
		doReturn(tks.getFirst()).when(tasks).save(any());
		doReturn(-1).when(tasks).dropTask(eq(-1L), eq(usr), eq(usr.getRole() == UserRole.ADMIN));
//		doReturn(tasks)
	}
	
	@BeforeAll
	
	public void preLoadContext() {
		/*
		 *  Использование здесь такого вызова обусловлено тем, 
		 *  что Mockito вызывает инициализацию @Mock только после @BeforeAll, 
		 *  соответственно использования в данном контексте @ExtendWith(MockitoExtension.class) 
		 *  будет приводить к NullPointer
		 */
		MockitoAnnotations.openMocks(this); 
		
		configurationTaskRep();
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
			var s = servTasks.getTask((long) i, usr);
			assertEquals(tks.get(i), s);
		}
		
		assertNull(servTasks.getTask((long) 1, null), "Was Null");
	}
	
	@Test
	@DisplayName("Test update")
	public void testUpdateTask() {
		for (int i = 0; i < COUNT_TASK; i++) {
			assertTrue(servTasks.updateTask(tks.get(i)));
		}
		
		assertFalse(servTasks.updateTask(null));
		assertFalse(servTasks.updateTask(Task.builder().title("test -1").author(usr).build()));
	}
	
	@Test
	@DisplayName("Test drop task")
	public void testDeleteTask() {
		for (int i = 0; i < COUNT_TASK; i++) {
			assertTrue(servTasks.deleteTask((long) i, usr));
		}
		
		assertFalse(servTasks.deleteTask( -1L, usr));
		assertFalse(servTasks.deleteTask( -1L, User.builder().email("test@some").build()));
	}
	
	
	
}
