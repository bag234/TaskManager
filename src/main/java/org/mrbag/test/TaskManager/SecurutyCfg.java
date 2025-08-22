package org.mrbag.test.TaskManager;

import org.mrbag.test.TaskManager.Service.Secure.JwtAuthFilter;
import org.mrbag.test.TaskManager.Service.Secure.UserConvertDetalisServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurutyCfg {
	
	@Autowired
	UserConvertDetalisServer detalis;
	
	@Autowired
	JwtAuthFilter filter;
	
	@Bean
	SecurityFilterChain configureFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(c -> c.disable())
			.headers(h -> h.frameOptions(f -> f.disable())) //h2-panel 
			.authorizeHttpRequests(t -> t
				.requestMatchers("/api/auth/**", "/h2/**", "/h2").permitAll()
				.anyRequest().authenticated()
					)
			.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authenticationProvider(getAuthProvider())
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	PasswordEncoder getBCryptPassEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider getAuthProvider() {
		 DaoAuthenticationProvider dao = new DaoAuthenticationProvider(detalis);
		 dao.setPasswordEncoder(getBCryptPassEncoder());
		 return dao;
	}
	
	@Bean
	AuthenticationManager getManager(AuthenticationConfiguration conf) throws Exception {
		AuthenticationManager manger = conf.getAuthenticationManager();
		return manger;
	}
	
}
