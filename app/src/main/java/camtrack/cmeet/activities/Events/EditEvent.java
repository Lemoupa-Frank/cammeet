package camtrack.cmeet.activities.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditEvent extends AppCompatActivity
{
    User user;
    Dialog dialog;
   public static ArrayList<String> Selected_Event_attendeeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditEventBinding activityEditEventBinding = ActivityEditEventBinding.inflate(getLayoutInflater());
        user = MainActivity.getuser();
        dialog = new Dialog(this);
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
            RM.meetings(R,MainActivity.cmeet_event_list.get(ClickedItem), delaydialog, EditEvent.this);
        });
        activityEditEventBinding.newparticipant.setOnClickListener(c->
        {
            Dialog event_dial;
            ArrayList<String> attendee;
            event_dial = newparticipantDialog();
            event_dial.show();
            TextView newatt  = event_dial.findViewById(R.id.newattendee);
            if(MainActivity.cmeet_event_list.get(ClickedItem).getAttendee() != null)
            { attendee = new ArrayList<>(Arrays.asList(MainActivity.cmeet_event_list.get(ClickedItem).getAttendee()));}
            else{attendee = new ArrayList<>();}
            event_dial.findViewById(R.id.add_email).setOnClickListener(view ->
            {
                if(newatt.getText()  != null)
                {
                    if(attendee.contains(newatt.getText().toString()))
                    {
                        Toast.makeText(this,"Attendee Already Present",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        attendee.add(newatt.getText().toString());
                    }
                    newatt.setText(null);
                }
                Toast.makeText(this,"checked Clicked",Toast.LENGTH_LONG).show();
            });
            event_dial.findViewById(R.id.valid).setOnClickListener(validate->
            {
                MainActivity.cmeet_event_list.get(ClickedItem).setAttendee(attendee.toArray(new String[0]));
                Viewattendeesfragment Nfragment = new Viewattendeesfragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.editattendeesfragment, Nfragment)
                        .commit();
                Toast.makeText(this,"valid Clicked",Toast.LENGTH_LONG).show();
            });
            event_dial.findViewById(R.id.cancel).setOnClickListener(cancel->{
                event_dial.cancel();
            });
        });

        activityEditEventBinding.clear.setOnClickListener(v-> {finish();});
        setContentView(activityEditEventBinding.getRoot());
    }
    public Dialog newparticipantDialog() {
        dialog.setContentView(R.layout.add_participant_dialog);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Objects.requireNonNull(dialog.getWindow()).setLayout(5 * (width) / 7, 1618 * (width) / 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
//Implement the server side of update_meetings
//Implement ways to update modifications made