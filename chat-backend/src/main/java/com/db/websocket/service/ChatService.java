package com.db.websocket.service;

import com.db.websocket.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final SimpUserRegistry simpUserRegistry;

	public ChatService(SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.simpUserRegistry = simpUserRegistry;
	}

	public void sendPublicMessage(Message message){
		simpMessagingTemplate.convertAndSend("/topic/public", message);
	}

	public void sendPrivateMessage(Message message, String receiverId, String senderId){
		simpMessagingTemplate.convertAndSend("/user/" + senderId + "/topic/private", message);
		if (!receiverId.equalsIgnoreCase(senderId)) {
			simpMessagingTemplate.convertAndSend("/user/" + receiverId + "/topic/private", message);
		}
	}

	public void sendActiveUserList(Map<String, String> userNameIdMap) {
		simpMessagingTemplate.convertAndSend("/topic/users", userNameIdMap);
	}

	public String sendPublicMessageREST(Message message) {
		simpMessagingTemplate.convertAndSend("/topic/public", message);
		return "Message broadcast to all users";
	}

	public String sendPrivateMessageREST(String receiverId, Message message) {
		simpMessagingTemplate.convertAndSend("/user/" + receiverId + "/topic/private", message);
		return "Message sent to " + receiverId;
	}

	public List<String> getActiveUserList() {
		return simpUserRegistry.getUsers().stream().map(simpUser -> simpUser.getName()).collect(Collectors.toList());
	}

	public void sendNewUserNotification(Message message) {
		simpMessagingTemplate.convertAndSend("/topic/public", message);
	}
}
