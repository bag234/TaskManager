package org.mrbag.test.TaskManager.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mrbag.test.TaskManager.Entity.Duty.TaskJsonDeserelizator;
import org.mrbag.test.TaskManager.Entity.Duty.UserSimpleJsonSerilizator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(using = TaskJsonDeserelizator.class)
@Schema(name = "Задача")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	
	long id;
	
	@Column(nullable = false)
	@Schema(description = "Заголовок задачи", example = "Реализовать аутентификацию")
	String title;
	
	@Schema(description = "Описание задачи", example = "Нужно подключить JWT и настроить фильтры безопасности")
	String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	@Schema(description = "Статус задачи", example = "TODO")
	TaskStatus status = TaskStatus.TODO;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	@Schema(description = "Приоритет задачи", example = "HIGH")
	TaskPriority priority = TaskPriority.LOW;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AUTHOR", nullable = false)
	@JsonSerialize(using = UserSimpleJsonSerilizator.class)
	@Schema(description = "Автор задачи (id пользователя)", implementation = Long.class, example = "42")
	User author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ASSIGNER")
	@JsonSerialize(using = UserSimpleJsonSerilizator.class)
	@Schema(description = "Назначенный исполнитель (id пользователя)", implementation = Long.class, example = "77")
	User assigner;
	
	@Column(name = "TIME_CREATE" ,nullable = false, updatable = false)
	@CreationTimestamp
	@Schema(description = "Дата создания", example = "2025-08-24T12:34:56")
	LocalDateTime createdAt; 
	
	@Column(name = "TIME_UPDATE", nullable = false)
	@UpdateTimestamp
	@Schema(description = "Дата обновления", example = "2025-08-24T13:45:00")
	LocalDateTime updateAt;
	
}
