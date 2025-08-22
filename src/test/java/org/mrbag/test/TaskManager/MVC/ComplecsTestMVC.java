package org.mrbag.test.TaskManager.MVC;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mrbag.test.TaskManager.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

//@JsonTest
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ComplecsTestMVC {

	@Autowired
	MockMvc mvc;
	
	@Autowired
	ObjectMapper mapper; 
	
	static User u1 = User.builder().username("test1").email("test1@test").password("test").build(),
				u2 = User.builder().username("test2").email("test2@test").password("test").build();
	
	@BeforeAll
	public static void LoadContext() {
		log.info("Start Registration test user");
		//TODO Regestration and Authefication
		
	}
	
	// Check re
	
	@Test
	public void testTest() throws JsonProcessingException {
		User u = User.builder().username("terest").email("terast@test").password("test").build();
		System.out.println(mapper.writeValueAsString(u));
	}
	
}
