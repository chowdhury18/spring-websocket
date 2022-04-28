package com.db.websocket.controller;

import com.db.websocket.model.Message;
import com.db.websocket.service.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaProducerController {
	private final KafkaProducerService kafkaProducerService;

	public KafkaProducerController(KafkaProducerService kafkaProducerService) {
		this.kafkaProducerService = kafkaProducerService;
	}

	@PostMapping(value = "/chat")
	public void sendMessage(@RequestBody Message message){
		kafkaProducerService.sendMessage(message);
	}
}
