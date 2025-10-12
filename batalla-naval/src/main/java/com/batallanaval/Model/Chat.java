package com.batallanaval.Model;

import java.time.Instant;

public class Chat {
    private String juegoId;
    private String from;
    private String content;
    private Instant timestamp;

    public Chat() {}
    public Chat(String juegoId, String from, String content, Instant timestamp) {
        this.juegoId = juegoId; this.from = from; this.content = content; this.timestamp = timestamp;
    }

    public String getJuegoId() { return juegoId; }
    public void setJuegoId(String juegoId) { this.juegoId = juegoId; }
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

