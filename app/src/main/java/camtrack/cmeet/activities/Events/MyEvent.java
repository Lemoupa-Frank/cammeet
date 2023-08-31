package camtrack.cmeet.activities.Events;

import com.google.api.client.json.JsonPolymorphicTypeMap;
import com.google.api.services.calendar.model.Events;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import com.google.api.services.calendar.model.Event;
import com.google.api.client.util.DateTime;
import java.sql.Date;
import java.util.List;

import retrofit2.http.Header;
import retrofit2.http.Headers;

// bad request originates from events not being able to be sent
public class MyEvent implements Serializable{
    public event_model evm;
    public String userid;

    public MyEvent(event_model evm, String userid) {
        this.evm = evm;
        this.userid = userid;
    }
}
