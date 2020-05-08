package com.redf.chatwebapp.messaging;

public class RemoveRoomMemberMessage {

    private String memberId;
    private String roomId;

    public RemoveRoomMemberMessage() {
    }


    public RemoveRoomMemberMessage(String memberId, String roomId) {
        setMemberId(memberId);
        setRoomId(roomId);
    }

    public String getMemberId() {
        return memberId;
    }


    private void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public String getRoomId() {
        return roomId;
    }


    private void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
