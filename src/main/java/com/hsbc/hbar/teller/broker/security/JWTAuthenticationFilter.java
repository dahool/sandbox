package com.hsbc.hbar.teller.broker.security;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

	private final JWTTokenHelper tokenHelper;
	
	public JWTAuthenticationFilter(JWTTokenHelper tokenHelper) {
		this.tokenHelper = tokenHelper;
	}
	
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		
		final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
			log.warn("Authorization Bearer empty from {}", request.getRemoteAddr());
			return null;
		}

		final String token = authorizationHeader.split(" ")[1].trim();
		// validate TOKEN
		Optional<String> validation = this.tokenHelper.verify(token); 
		if (validation.isEmpty()) {
			log.error("Token verification failed from {}", request.getRemoteAddr());
			return null;			
		}
		
		log.info("Validation successful. {}", validation.get());
		
		return validation.get();
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}
	
}
