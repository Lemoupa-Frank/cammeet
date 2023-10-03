package camtrack.cmeet.activities.UserMeetings;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class UserMeetings implements Serializable {

    private static final long serialVersionUID = 1L;

    protected UserMeetingsPK userMeetingsPK;

    private String signature;
// Replace with userid, this will be send to the server to append it to the uris
    private String role;
    private Boolean signable;

    byte[] signature_data;

    public byte[] getSignature_data() {
        return signature_data;
    }

    public void setSignature_data(byte[] signature_data) {
        this.signature_data = signature_data;
    }

    public UserMeetings() {
    }

    public UserMeetings(UserMeetingsPK userMeetingsPK) {
        this.userMeetingsPK = userMeetingsPK;
    }


    public UserMeetingsPK getUserMeetingsPK() {
        return userMeetingsPK;
    }

    public void setUserMeetingsPK(UserMeetingsPK userMeetingsPK) {
        this.userMeetingsPK = userMeetingsPK;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getSignable() {
        return signable;
    }

    public void setSignable(Boolean signable) {
        this.signable = signable;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userMeetingsPK != null ? userMeetingsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserMeetings))
        {
            return false;
        }
        UserMeetings other = (UserMeetings) object;
        return (this.userMeetingsPK != null || other.userMeetingsPK == null) && (this.userMeetingsPK == null || this.userMeetingsPK.equals(other.userMeetingsPK));
    }

    @NonNull
    @Override
    public String toString() {
        return "com.frs.customer.UserMeetings[ userMeetingsPK=" + userMeetingsPK + " ]";
    }

}

