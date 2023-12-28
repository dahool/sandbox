package com.hsbc.hbar.teller.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApplicationSubListener {

	@Autowired
	private StatsHolder stats;
	
	@EventListener(classes = { SessionSubscribeEvent.class })
	public void handleSubscribe() {
		log.debug("Client connected");
		stats.addSubscribedClients();
	}
	
	@EventListener(classes = { SessionUnsubscribeEvent.class, SessionDisconnectEvent.class })
	public void handleUnsubscribe() {
		log.debug("Client disconnected");
		stats.removeSubscribedClients();
	}
	
}
