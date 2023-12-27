package com.hsbc.hbar.teller.broker.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hsbc.hbar.teller.broker.security.JWTAuthenticationDetailsSource;
import com.hsbc.hbar.teller.broker.security.JWTAuthenticationFilter;
import com.hsbc.hbar.teller.broker.security.JWTAuthenticationManager;
import com.hsbc.hbar.teller.broker.security.JWTTokenHelper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTTokenHelper jwtTokenHelper) throws Exception {
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		});
		
		http.authorizeHttpRequests((authorize) -> {
			authorize.antMatchers("/api/**").authenticated()
			.anyRequest().permitAll();
		});
		
		JWTAuthenticationFilter jwtFilter = new JWTAuthenticationFilter(jwtTokenHelper);
		jwtFilter.setAuthenticationManager(new JWTAuthenticationManager());
		jwtFilter.setAuthenticationDetailsSource(new JWTAuthenticationDetailsSource());
		
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.csrf().disable();
		
		return http.build();
    }
	
	@Bean
	public JWTTokenHelper jwtTokenHelper() {
		return new JWTTokenHelper();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")
						.allowedHeaders("*")
						.allowedOrigins("*")
						.allowedMethods("*");
			}
		};
		
	}
	
}
