package org.mrbag.test.TaskManager.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mrbag.test.TaskManager.Entity.Duty.UserSimpleJsonSerilizator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	long id;
	
	@Column(nullable = false)
	String title;
	
	String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	TaskStatus status = TaskStatus.TODO;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	TaskPriority priority = TaskPriority.LOW;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AUTHOR", nullable = false)
	@JsonSerialize(using = UserSimpleJsonSerilizator.class)
	User author;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ASSIGNER")
	@JsonSerialize(using = UserSimpleJsonSerilizator.class)
	User assigner;
	
	@Column(name = "TIME_CREATE" ,nullable = false, updatable = false)
	@CreationTimestamp
	LocalDateTime createdAt; 
	
	@Column(name = "TIME_UPDATE", nullable = false)
	@UpdateTimestamp
	LocalDateTime updateAt;
	
}
