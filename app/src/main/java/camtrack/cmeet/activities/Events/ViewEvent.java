package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;


import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityViewEventBinding;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Retrofit;
// if the textviews are wrap content then if i set the picture and text only at runtime them i might achieve vanishing effect
// Any conversion to String must be checked for nul  pointers

public class ViewEvent extends AppCompatActivity {
    ActivityViewEventBinding viewEventBinding;
    int Selected_Event; Retrofit retrofitobj;
    public List<Event> a = MainActivity.items();
    static public List<UserMeetings> LUM;
    public List<event_model> event_List = MainActivity.get_cmeet_event_list();
    Event event ;
    event_model eventmodel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        User user = MainActivity.getuser();
        super.onCreate(savedInstanceState);
        Selected_Event = ClickedItem;
        viewEventBinding = ActivityViewEventBinding.inflate(getLayoutInflater());
        this.eventmodel = event_List.get(Selected_Event);
        viewEventBinding.startsigning.setVisibility(eventmodel.getOwner().equals(user.getUserId())?View.VISIBLE:View.INVISIBLE);
        {
            Viewattendeesfragment fragment = new Viewattendeesfragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.attendeesfragment, fragment)
                    .commit();
        }
        viewEventBinding.startsigning.setOnClickListener(v->
        {
            Toast.makeText(ViewEvent.this,"I the Owner",Toast.LENGTH_LONG).show();
        });
        setContentView(viewEventBinding.getRoot());

        viewEventBinding.Summary.setText(event_List.get(Selected_Event).getTitle()==null?" ":event_List.get(Selected_Event).getTitle());
        viewEventBinding.location.setText(event_List.get(Selected_Event).getLocation()==null?" ":event_List.get(Selected_Event).getLocation());
        viewEventBinding.Owner.setText(event_List.get(Selected_Event).getOwner()==null?" ":event_List.get(Selected_Event).getOwner());
        viewEventBinding.Description.setText(event_List.get(Selected_Event).getDescription()==null?" ":event_List.get(Selected_Event).getDescription());


        viewEventBinding.edit.setOnClickListener(v->
        {
            retrofitobj = Retrofit_Base_Class.getClient();
          if(check_owner(user.getUserId(),a.get(Selected_Event).getOrganizer().getEmail()))
          {
              Intent a = new Intent(ViewEvent.this,EditEvent.class);
              startActivity(a);
          }
          else
          {
              Toast.makeText(this, R.string.refuse_event_write_permission, Toast.LENGTH_LONG).show();
          }

        });
        viewEventBinding.clear.setOnClickListener(v->
        {
            finish();
        });
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
     * @param ed Edittext to be checked if filled
     * @param S string to be set to Editext
     * @param B boolean to see if the edittext has content
     */
    public void Set_Invisible_on_Empty(TextView ed, String S, Boolean B)
    {
        if(B)
        {
            ed.setText(S);
        }
        {
            ed.setVisibility(View.INVISIBLE);
        }
    }
}