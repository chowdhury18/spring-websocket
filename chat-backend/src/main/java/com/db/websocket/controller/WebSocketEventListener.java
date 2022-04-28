package com.db.websocket.controller;

import com.db.websocket.model.Message;
import com.db.websocket.model.MessageType;
import com.db.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {
	@Autowired
	private SimpMessageSendingOperations sendingOperations;
	@Autowired
	private ChatController chatController;
	@Autowired
	private ChatService chatService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectedEvent){
		log.info("Connection established.....");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent){
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
		String username = (String) stompHeaderAccessor.getSessionAttributes().get("username");
		Message message = Message.builder().type(MessageType.DISCONNECT).sender(username).build();
		chatController.userNameIdMap.remove(username); // temporary solution
		chatService.sendActiveUserList(chatController.userNameIdMap); // temporary solution
		sendingOperations.convertAndSend("/topic/public", message);
	}
}
