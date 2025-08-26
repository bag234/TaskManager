package org.mrbag.test.TaskManager.MVC;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mrbag.test.TaskManager.Entity.Task;
import org.mrbag.test.TaskManager.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestInstance(Lifecycle.PER_CLASS)
public class ComplecsTestMVC {

	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper mapper; 
	
	static User u1 = User.builder().username("test1").email("test1@test").password("test").build(),
				u2 = User.builder().username("test2").email("test2@test").password("test").build();
	
	static String jwt1, jwt2;
	
	@BeforeAll
	public void LoadContext() throws Exception {
		log.info("Start Registration test user");
		assertTrue(
				mvc.perform(post("/api/auth/register").content(mapper.writeValueAsString(u1)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("true"),
				"Error 1 User registration"
		);
		assertTrue(
				mvc.perform(post("/api/auth/register").content(mapper.writeValueAsString(u2)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("true"),
				"Error 2 User registration"
		);
		log.info("Start auth test user");
		jwt1 = mvc.perform(post("/api/auth/login").content(mapper.writeValueAsString(u1)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		jwt2 = mvc.perform(post("/api/auth/login").content(mapper.writeValueAsString(u2)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		log.info("Test auth test user");
		assertTrue(jwt1.startsWith("{\"token\":"));
		assertTrue(jwt2.startsWith("{\"token\":"));
		log.info("Save auth token user");
		jwt1 = jwt1.replace("{\"token\":\"", "").replace("\"}", "");
		jwt2 = jwt2.replace("{\"token\":\"", "").replace("\"}", "");
		
	}
	
	@Test
	public void testUserInformation() throws Exception {
		String usr1 = mvc.perform(get("/api/user/me").header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		String usr2 = mvc.perform(get("/api/user/me").header("Authorization", "Bearer " + jwt2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		User user1 = mapper.readValue(usr1, User.class);
		User user2 = mapper.readValue(usr2, User.class);
		
		assertTrue(equlasUser(u1, user1) ,"Wrong auth token user 1");
		assertTrue(equlasUser(u2, user2) ,"Wrong auth token user 2");
		
	}
	
	@Test
	public void testTaskMethodWorkAndBareerRules() throws Exception {
		Task t1 = Task.builder().title("test1").description("test1").build();
		Task t2 = Task.builder().title("test2").description("test2").build();
		
		Task ts1 = mapper.readValue(mvc.perform(post("/api/tasks").content(mapper.writeValueAsString(t1)).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201)).andReturn().getResponse().getContentAsString(), Task.class);
		Task ts2 = mapper.readValue(mvc.perform(post("/api/tasks").content(mapper.writeValueAsString(t2)).header("Authorization", "Bearer " + jwt2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(201)).andReturn().getResponse().getContentAsString(), Task.class);
		
		assertTrue(equlasTask(t1, ts1), "Task1 not equlas");
		assertTrue(equlasTask(t2, ts2), "Task2 not equlas");
		
		t1 = mapper.readValue(
				mvc.perform(get("/api/tasks/" + ts1.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString(), 
				Task.class);
		
		mvc.perform(get("/api/tasks/" + ts2.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(400));
		
		mapper.readValue(
				mvc.perform(get("/api/tasks/" + ts1.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString(), 
				Task.class);
		
		mvc.perform(put("/api/tasks/" + ts1.getId()).content(mapper.writeValueAsString(t2)).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(202));
		
		t1 = mapper.readValue(
				mvc.perform(get("/api/tasks/" + ts1.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(200)).andReturn().getResponse().getContentAsString(), 
				Task.class);
		assertTrue(equlasTask(t1, t2), "Task1 not equlas");
		mvc.perform(delete("/api/tasks/" + ts1.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(202));
		
		mvc.perform(get("/api/tasks/" + ts1.getId()).header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400));
		
	}
	
	@Test
	@DisplayName("Testing security token auth and baerer")
	public void testSecurity() throws Exception {
		mvc.perform(get("/api/user/me").header("Authorization", "Bearer " + jwt1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		mvc.perform(get("/api/user/me").header("Authorization", "Bearer " + jwt2).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		mvc.perform(get("/api/user/me").header("Authorization", "Bearer Wrong-TOKEN-test").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(403));
		
		mvc.perform(get("/api/user/me").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(403));
	}
	
	private static boolean equlasTask(Task t1, Task t2) {
		return t1.getTitle().equals(t2.getTitle()) &&
				t1.getDescription().equals(t2.getDescription()) &&
				t1.getPriority() == t2.getPriority() &&
				t1.getStatus() == t2.getStatus();
	}
	
	private static boolean equlasUser(User u1, User u2) {
		return u1.getEmail().equals(u2.getEmail()) && u1.getUsername().equals(u2.getUsername());
	}
	
}
