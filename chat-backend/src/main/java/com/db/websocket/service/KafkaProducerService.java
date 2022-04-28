package com.db.websocket.service;

import com.db.websocket.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
	@Value(value = "${spring.kafka.topic}")
	private String topic;

	private final KafkaTemplate<String, Message> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(Message message){
		kafkaTemplate.send(topic, message);
	}
}
