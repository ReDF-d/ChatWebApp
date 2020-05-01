package com.redf.chatwebapp.messaging;

public class EditChatTitleMessage {
    private String roomId;
    private String title;

    public EditChatTitleMessage() {
    }


    public EditChatTitleMessage(String roomId, String title) {
        setRoomId(roomId);
        setTitle(title);
    }


    public String getRoomId() {
        return roomId;
    }


    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
