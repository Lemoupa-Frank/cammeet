package camtrack.cmeet.activities.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityEditEventBinding;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Retrofit;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;

import java.util.ArrayList;
import java.util.List;

public class EditEvent extends AppCompatActivity
{
    User user;
   public static ArrayList<String> Selected_Event_attendeeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditEventBinding activityEditEventBinding = ActivityEditEventBinding.inflate(getLayoutInflater());
        user = MainActivity.getuser();
        activityEditEventBinding.editDescription.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getDescription());
        activityEditEventBinding.editlocation.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getLocation());
        activityEditEventBinding.editSummary.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getTitle());

            Viewattendeesfragment fragment = new Viewattendeesfragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.editattendeesfragment, fragment)
                    .commit();

        activityEditEventBinding.save.setOnClickListener(v->
        {
            MainActivity.cmeet_event_list.get(ClickedItem).setTitle(activityEditEventBinding.editSummary.getText().toString());
            MainActivity.cmeet_event_list.get(ClickedItem).setLocation(activityEditEventBinding.editlocation.getText().toString());
            MainActivity.cmeet_event_list.get(ClickedItem).setDescription(activityEditEventBinding.editDescription.getText().toString());
            MainActivity.cmeet_event_list.get(ClickedItem).setAttendee(fragment.getAttendeeList().toArray(new String[0]));
            Retrofit R = Retrofit_Base_Class.getClient();
            Request_Maker RM = new Request_Maker();
            Dialog delaydialog = cmeet_delay.delaydialogCircular(this);
            RM.update_meetings(R,MainActivity.cmeet_event_list.get(ClickedItem), delaydialog, EditEvent.this);
        });
        activityEditEventBinding.clear.setOnClickListener(v-> {finish();});
        setContentView(activityEditEventBinding.getRoot());
    }
}
//Implement the server side of update_meetings