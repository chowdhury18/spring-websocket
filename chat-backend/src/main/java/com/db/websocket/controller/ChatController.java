package com.db.websocket.controller;

import com.db.websocket.model.Message;
import com.db.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ResponseBody
@Slf4j
public class ChatController {
	private final ChatService chatService;
	public final Map<String, String> userNameIdMap = new HashMap<>();

	public ChatController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
		this.chatService = chatService;
	}

	@MessageMapping("/chat.send")
	public void sendPublicMessage(@Payload Message message) {
		chatService.sendPublicMessage(message);
	}

	@MessageMapping("/chat.private.send.{receiverId}")
	public void sendPrivateMessage(@Payload Message message, @DestinationVariable("receiverId") String receiverId, Principal principal) {
		chatService.sendPrivateMessage(message, receiverId, principal.getName());
	}

	@MessageMapping("/chat.newUser")
	public void newUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
		userNameIdMap.put(message.getSender(), principal.getName());
		headerAccessor.getSessionAttributes().put("username", message.getSender());
		chatService.sendActiveUserList(userNameIdMap);
		chatService.sendNewUserNotification(message);
	}

	@PostMapping(value = "/public")
	public String sendPublicMessageREST(@RequestBody Message message) {
		return chatService.sendPublicMessageREST(message);
	}

	@PostMapping(value = "/private-messages/{id}")
	public String sendPrivateMessageREST(@PathVariable String id, @RequestBody Message message) {
		return chatService.sendPrivateMessageREST(id, message);
	}

	@GetMapping(value = "/users")
	public List<String> getActiveUserList() {
		return chatService.getActiveUserList();
	}
}
