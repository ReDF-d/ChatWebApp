package com.redf.chatwebapp.websocket;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    //private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    private SimpMessageSendingOperations messagingTemplate;


    @EventListener
    public void handleWebSocketConnectListener(@NotNull SessionConnectedEvent event) {

    }

    @EventListener
    public void handleWebSocketDisconnectListener(@NotNull SessionDisconnectEvent event) {
    /*    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long id = (Long) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("id");
        String roomId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("roomId");
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(ChatMessage.MessageType.LEAVE);
        chatMessage.setSender(username);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage);*/
    }
}
