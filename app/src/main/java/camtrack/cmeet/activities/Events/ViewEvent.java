package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityViewEventBinding;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import camtrack.cmeet.websocket.Message;
import camtrack.cmeet.websocket.webSocketClient;
import retrofit2.Retrofit;
// if the textviews are wrap content then if i set the picture and text only at runtime them i might achieve vanishing effect
// Any conversion to String must be checked for nul  pointers

public class ViewEvent extends AppCompatActivity {
    ActivityViewEventBinding viewEventBinding;
    int Selected_Event; Retrofit retrofitobj;
    public List<Event> a = MainActivity.items();
    static public List<UserMeetings> LUM;
    public List<event_model> event_List = MainActivity.get_cmeet_event_list();
    private webSocketClient WebSocketClients;
    boolean Signable;
    Message startSign;
    Event event ;
    event_model eventmodel;
    URI serverUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        User user = MainActivity.getuser();
        super.onCreate(savedInstanceState);
        Selected_Event = ClickedItem;
        viewEventBinding = ActivityViewEventBinding.inflate(getLayoutInflater());
        this.eventmodel = event_List.get(Selected_Event);
        if(check_owner(user.getUserId(),a.get(Selected_Event).getOrganizer().getEmail())) {viewEventBinding.edit.setVisibility(View.VISIBLE);}
        try
        {
            serverUri = new URI("ws://192.168.43.108:8080");
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        final webSocketClient wb = new webSocketClient(serverUri);
        wb.connect();
        WebSocketClients = wb;


        viewEventBinding.startsigning.setVisibility(eventmodel.getOwner().equals(user.getUserId())?View.VISIBLE:View.INVISIBLE);


            Viewattendeesfragment fragment = new Viewattendeesfragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.attendeesfragment, fragment)
                    .commit();




        viewEventBinding.Summary.setText(event_List.get(Selected_Event).getTitle()==null?" ":event_List.get(Selected_Event).getTitle());
        viewEventBinding.location.setText(event_List.get(Selected_Event).getLocation()==null?" ":event_List.get(Selected_Event).getLocation());
        viewEventBinding.Owner.setText(event_List.get(Selected_Event).getOwner()==null?" ":event_List.get(Selected_Event).getOwner());
        viewEventBinding.Description.setText(event_List.get(Selected_Event).getDescription()==null?" ":event_List.get(Selected_Event).getDescription());

        viewEventBinding.startsigning.setOnClickListener(v->
        {
            //think about connect in this very button and add delay to obtain server return
            if(wb.isOpen())
            {
                startSign = new Message();
                startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
                startSign.setSignable(true);
                startSign.setSender(user.getUserId());
                wb.send(startSign.toJson());
            }
            else
            {
                recreate();
                Toast.makeText(ViewEvent.this,"An error occured please try again",Toast.LENGTH_LONG).show();
            }
        });
        viewEventBinding.edit.setOnClickListener(v->
        {
            retrofitobj = Retrofit_Base_Class.getClient();

              Intent a = new Intent(ViewEvent.this,EditEvent.class);
              startActivity(a);

        });
        viewEventBinding.sign.setOnClickListener(v->
        {
            Toast.makeText(this,"You have Signed theh event",Toast.LENGTH_LONG).show();
            Toast.makeText(this, fragment.getAttendeeList().toString(), Toast.LENGTH_SHORT).show();
            startSign = new Message();
            startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
            startSign.setSender(user.getUserId());
            if(wb.isOpen())
            { wb.send(startSign.toJson());}
        });
        wb._Message.observe(this,v->
        {

            JsonObject json = JsonParser.parseString(wb._Message.getValue()).getAsJsonObject();
            if(json.size() != 0)
            {
                String Sender = json.get("sender").getAsString();
                if(Sender.equals(event_List.get(Selected_Event).getOwner()))
                {
                    Signable = json.get("Signable").getAsBoolean();
                }
                if(Signable)
                {
                    viewEventBinding.sign.setVisibility(View.VISIBLE);
                }
                if(!Signable)
                {
                    viewEventBinding.sign.setVisibility(View.INVISIBLE);
                }
                {
                    fragment.update_signature(fragment.getAttendeePosition(Sender));
                }
                Toast.makeText(this,"You can Sign Permitted by:" + Sender,Toast.LENGTH_LONG).show();
            }
            // think about making signable false when meeting ends
        });
        setContentView(viewEventBinding.getRoot());

    }

    @Override
    protected void onDestroy()
    {
        if(WebSocketClients.isOpen())
        {
            WebSocketClients.close();
        }
        super.onDestroy();
    }


    /**Check if the Owner possesses a write permission on the event
     *
     * @param User - The user of the present session
     * @param Owner - The owner or Organizer of the mail
     * @return - A boolean to know if user has write permission
     */
    private boolean check_owner(String User, String Owner)
    {
        return User.equals(Owner);
    }

    public static String getCleanedDisplayNames(List<EventAttendee> attendeesList) {
        StringBuilder cleanedNames = new StringBuilder();

        for (EventAttendee attendee : attendeesList) {
            String displayName = attendee.getEmail();
            cleanedNames.append(displayName).append("\n\n");
        }

        return cleanedNames.toString();
    }

    /**
     * For now it doesnt serve much as setting an Edittext invisible
     * does not close the space it occupied
     * @param edittext Edittext to be checked if filled
     * @param String string to be set to Editext
     * @param Boolean boolean to see if the edittext has content
     */
    public void Set_Invisible_on_Empty(TextView edittext, String String, Boolean Boolean)
    {
        if(Boolean)
        {
            edittext.setText(String);
        }
        {
            edittext.setVisibility(View.INVISIBLE);
        }
    }


}
// If a user enters after the message has been broadcasted he would not be able to sign
// so what you can do it to store the owners sign message and perhaps for every new connection
// broadcast the owners sign message, or use the other signing to make signablee