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
import camtrack.cmeet.databinding.ActivityViewEventBinding;
// if the textviews are wrap content then if i set the picture and text only at runtime them i might achieve vanishing effect
// Any conversion to String must be checked for nul  pointers

public class ViewEvent extends AppCompatActivity {
    ActivityViewEventBinding viewEventBinding;
    int Selected_Event;
    public List<Event> a = MainActivity.items();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Selected_Event = ClickedItem;
        viewEventBinding = ActivityViewEventBinding.inflate(getLayoutInflater());
        setContentView(viewEventBinding.getRoot());
        viewEventBinding.Summary.setText(a.get(Selected_Event).getSummary()==null?" ":a.get(Selected_Event).getSummary());
        viewEventBinding.location.setText(a.get(Selected_Event).getLocation()==null?" ":a.get(Selected_Event).getLocation());
        viewEventBinding.Owner.setText(a.get(Selected_Event).getOrganizer().getEmail()==null?" ":a.get(Selected_Event).getOrganizer().getEmail());
        viewEventBinding.participants.setText(a.get(Selected_Event).getAttendees()==null?" ":getCleanedDisplayNames(a.get(Selected_Event).getAttendees()));
        viewEventBinding.Description.setText(a.get(Selected_Event).getDescription()==null?" ":a.get(Selected_Event).getDescription());
        //a.get(Selected_Event).getStart()
        //a.get(Selected_Event).getEnd()
        viewEventBinding.edit.setOnClickListener(v->
        {
          if(check_owner("frankmichel022@gmail.com",a.get(Selected_Event).getOrganizer().getEmail()))
          {
              Toast.makeText(this, R.string.grant_event_write_permission, Toast.LENGTH_LONG).show();
          }
          else
          {
              Toast.makeText(this, R.string.refuse_event_write_permission, Toast.LENGTH_LONG).show();
          }
          Intent a = new Intent(ViewEvent.this,Edit_Activity.class);
          startActivity(a);
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
    public String getCleanedDisplayNames(List<EventAttendee> attendeesList) {
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
    public void EditText_Checker(TextView ed, String S, Boolean B)
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