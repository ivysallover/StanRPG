package com.batallanaval.Controller;

import com.batallanaval.Model.Chat;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    public ChatController(SimpMessagingTemplate messagingTemplate) { this.messagingTemplate = messagingTemplate; }

    // El cliente envía a: /app/chat/{juegoId}
    @MessageMapping("/chat/{juegoId}")
    public void sendToRoom(@DestinationVariable String juegoId, Chat incoming) {
        if (incoming.getTimestamp() == null) incoming.setTimestamp(Instant.now());
        incoming.setJuegoId(juegoId);
        // Los suscriptores escuchan en: /topic/chat/{juegoId}
        messagingTemplate.convertAndSend("/topic/chat/" + juegoId, incoming);
    }
}

