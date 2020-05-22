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
    }
}
