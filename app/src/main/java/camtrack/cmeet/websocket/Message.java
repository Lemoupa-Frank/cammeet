package camtrack.cmeet.websocket;

import com.google.gson.Gson;

public class Message {
    private String sender;
    private String MeetingId;
    private Boolean Signable;

    public Boolean getSignable() {
        return Signable;
    }

    public void setSignable(Boolean signable) {
        Signable = signable;
    }

    

    // Constructors, getters, and setters

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(String meetingId) {
        this.MeetingId = meetingId;
    }

    public Message (String Sender, String MeetingId)
    {

    }
    public Message ()
    {

    }


    public String toJson() {
        return new Gson().toJson(this);
    }


    public static Message fromJson(String json) {
        return new Gson().fromJson(json, Message.class);
    }
}
