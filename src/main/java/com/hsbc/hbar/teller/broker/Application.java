package com.hsbc.hbar.teller.broker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder(Application.class);
		app.build().addListeners(new ApplicationPidFileWriter("./application.pid"));
		app.run(args);
		//SpringApplication.run(Application.class, args);
	}
	
}
