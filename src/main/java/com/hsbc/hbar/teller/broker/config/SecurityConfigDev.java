package com.hsbc.hbar.teller.broker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
public class SecurityConfigDev {

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		return http.build();
    }
	
}
