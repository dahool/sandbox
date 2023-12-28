package com.hsbc.hbar.teller.broker.web.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hsbc.hbar.teller.broker.StatsHolder;

@Controller
@RequestMapping("/app")
public class StatusController {

	@Autowired
	private StatsHolder stats;
	
	@GetMapping("status")
	public String status(Model model) {
		model.addAttribute("clients", stats.getSubscribedClients());
		model.addAttribute("requests", stats.getProcessedRequests());
		model.addAttribute("running", DurationFormatUtils.formatDurationWords(Duration.between(stats.getStarted(), LocalDateTime.now()).toMillis(), true, false));
		return "status";
	}
	
}
