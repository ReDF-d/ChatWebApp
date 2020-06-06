package com.redf.chatwebapp.messaging;

import org.jetbrains.annotations.Contract;

public class FriendNotification {

    private String sender;
    private String recipient;
    private String status;
    private String senderUsername;


    public FriendNotification(String sender, String recipient, String status, String senderUsername) {
        setSender(sender);
        setRecipient(recipient);
        setStatus(status);
        setSenderUsername(senderUsername);
    }

    @Contract(pure = true)
    public String getSender() {
        return sender;
    }

    private void setSender(String sender) {
        this.sender = sender;
    }

    @Contract(pure = true)
    public String getSenderUsername() {
        return senderUsername;
    }

    private void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    @Contract(pure = true)
    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    @Contract(pure = true)
    public String getRecipient() {
        return recipient;
    }

    private void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
