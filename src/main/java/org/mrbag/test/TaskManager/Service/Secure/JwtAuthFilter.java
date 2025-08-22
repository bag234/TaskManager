package org.mrbag.test.TaskManager.Service.Secure;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String HEADER = "Authorization";
	
	private static final String PREFIX = "Bearer ";
	
	@Autowired //TODO Fix name
	AuthifacationService service;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String auth_header = request.getHeader(HEADER);
		if (auth_header == null || auth_header.isEmpty() || !auth_header.startsWith(PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) service.singInWithToken(auth_header.replace(PREFIX, ""));
		
		auth.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(auth);
		filterChain.doFilter(request, response);
		
	}

}
