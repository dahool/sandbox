package com.hsbc.hbar.teller.broker.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.hbar.teller.broker.web.api.UserActionAuthorization;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/authorization")
@Slf4j
public class ActionAuthorizationController {

	@Value("${topic.token}")
	private String topicPrefix;
	
    @Autowired
    private SimpMessagingTemplate messageSender;
    
	@PostMapping("confirm")
	public ResponseEntity<Void> authorizeAction(@Valid @RequestBody UserActionAuthorization payload, BindingResult bindingResult) {
		log.debug("AuthorizeAction received payload {}", payload);
		if (bindingResult.hasErrors()) {
			log.warn("BadRequest. Payload {}", payload);
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);	
		}

		log.debug("Broadcast to {}", topicPrefix);
		messageSender.convertAndSend(topicPrefix, payload);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
