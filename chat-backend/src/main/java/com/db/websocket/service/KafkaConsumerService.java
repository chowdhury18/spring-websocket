package com.db.websocket.service;

import com.db.websocket.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
	private final SimpMessagingTemplate messagingTemplate;

	public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@KafkaListener(
			topics = "${spring.kafka.topic}",
			groupId = "${spring.kafka.group-id}")
	public void receiveMessage(Message message){
		log.info("Sender: " + message.getSender() + ", Message: " + message.getContent());
		messagingTemplate.convertAndSend("/topic/public", message);
	}
}
