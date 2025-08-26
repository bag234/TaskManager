package org.mrbag.test.TaskManager.Entity;

import java.util.Collection;
import java.util.List;

import org.mrbag.test.TaskManager.Entity.Duty.UserJSONSerelizator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TaskUser")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = UserJSONSerelizator.class)
@Schema(name = "Пользователь сервиса", hidden = true)
public class User implements UserDetails {
	
	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Schema(description = "Уникальный идентификатор пользователя", example = "123")
	long id;
	
	@Column(name = "login", nullable = false)
	@Size(min = 1, max = 50)
	@Schema(description = "Логин пользователя", example = "ivan")
	String username;
	
	@Column(nullable = false, unique = true)
	@NotNull
	@Email
	@Schema(description = "Email пользователя (Уникальный)", example = "ivan@example.com")
	String email;
	
	@Column(nullable = false)
	@JsonIgnore
	@NotNull
	@Size(min = 1, max = 50)
	@Schema(description = "Пароль (скрыт в документации)", accessMode = Schema.AccessMode.WRITE_ONLY, example = "Qwerty123!")
	String password; 
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	@Schema(description = "Роль пользователя в системе", example = "USER")
	UserRole role = UserRole.USER;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	
}
