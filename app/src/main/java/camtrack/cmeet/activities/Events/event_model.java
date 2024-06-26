package camtrack.cmeet.activities.Events;

import com.google.api.client.util.DateTime;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

public class event_model  implements Serializable{
    private String meetingId;

    private String location;

    private Integer numberOfParticipants;

    private String dateofcreation;

    private String startdate;

    private String enddate;

    private String owner;

    private String description;

    private String title;

    private String userid;

    private String meeting_update_time;

    private String update_comment;

    private String[] attendee;
    //MOve this datatype to an ArrayList maybe


    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getDateofcreation() {
        return dateofcreation;
    }

    public void setDateofcreation(String dateofcreation) {
        this.dateofcreation = dateofcreation;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAttendee() {
        return attendee;
    }

    public String getMeetingupdateTime() {
        return meeting_update_time;
    }

    public void setMeetingupdateTime(String meetingupdateTime) {
        this.meeting_update_time = meetingupdateTime;
    }

    public String getUpdateComment() {
        return update_comment;
    }

    public void setUpdateComment(String updateComment) {
        this.update_comment = updateComment;
    }

    public void setAttendee(String[] attendee) {
        this.attendee = attendee;
    }
}
