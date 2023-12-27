package com.hsbc.hbar.teller.broker.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.converter.GsonMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableAsync
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private TaskScheduler messageBrokerTaskScheduler;

	@Autowired
	public void setMessageBrokerTaskScheduler(@Lazy TaskScheduler taskScheduler) {
		this.messageBrokerTaskScheduler = taskScheduler;
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/broker").setAllowedOrigins("**");
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic/")
			.setHeartbeatValue(new long[] {10000, 20000})
			.setTaskScheduler(messageBrokerTaskScheduler);
		registry.setApplicationDestinationPrefixes("/app", "/api");
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		messageConverters.add(new GsonMessageConverter());
		messageConverters.add(new StringMessageConverter());
		return false;
	}
	
}
