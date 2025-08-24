package org.mrbag.test.TaskManager.Controller;

import org.mrbag.test.TaskManager.Entity.TaskPriority;
import org.mrbag.test.TaskManager.Entity.TaskStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/service")
@Tag(name = "Сервисный API", description = "Вспомогательные эндпоинты для фронтенда")
public class ServiceAPIController {

	@GetMapping("/status")
	@Operation(summary = "Получить список возможных статусов задачи", description = "Возвращает массив всех значений перечисления TaskStatus")
	@ApiResponse(responseCode = "200", description = "Список статусов успешно получен")
	public TaskStatus[] getAllPosibleStatus() {
		return TaskStatus.values();
	}

	@GetMapping("/priority")
	@Operation(summary = "Получить список возможных приоритетов задачи", description = "Возвращает массив всех значений перечисления TaskPriority")
	@ApiResponse(responseCode = "200", description = "Список приоритетов успешно получен")
	public TaskPriority[] getAllPosiblePriority() {
		return TaskPriority.values();
	}

}
