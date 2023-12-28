package com.hsbc.hbar.teller.broker;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class StatsHolder {

	private AtomicInteger subscribedClients = new AtomicInteger(0);
	
	private AtomicLong processedRequests = new AtomicLong(0);
	
	private LocalDateTime started = LocalDateTime.now();

	public int getSubscribedClients() {
		return subscribedClients.get();
	}

	public void removeSubscribedClients() {
		this.subscribedClients.decrementAndGet();
	}
	
	public void addSubscribedClients() {
		this.subscribedClients.incrementAndGet();
	}

	public long getProcessedRequests() {
		return processedRequests.get();
	}

	public void addProcessedRequests() {
		this.processedRequests.incrementAndGet();
	}

	public LocalDateTime getStarted() {
		return started;
	}

}
