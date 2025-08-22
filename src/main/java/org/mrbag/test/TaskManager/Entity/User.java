package org.mrbag.test.TaskManager.Entity;

import java.util.Collection;
import java.util.List;

import org.mrbag.test.TaskManager.Entity.Duty.UserJSONSerelizator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class User implements UserDetails {
	
	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	long id;
	
	@Column(name = "login", nullable = false)
	String username;
	
	@Column(nullable = false, unique = true)
	String email;
	
	@Column(nullable = false)
	@JsonIgnore
	String password; 
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@Builder.Default
	UserRole role = UserRole.USER;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	
}
