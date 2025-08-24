package org.mrbag.test.TaskManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwagerCfg {
	
	@Bean
	OpenAPI configOpenApi() {
		
		Info info = new Info()
				.title("RESTful-сервис для управления задачами")
				.version("1.0")
				.description("Простой сервис управления задач со ограничением доступом при помощи jwt токенов");
		
		
		return new OpenAPI().info(info);
	}
	
}
