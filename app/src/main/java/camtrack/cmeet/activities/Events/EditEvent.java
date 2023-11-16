package camtrack.cmeet.activities.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityEditEventBinding;
import camtrack.cmeet.databinding.FrameMainBinding;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditEvent extends AppCompatActivity
{
    User user;
    private List<event_model> event_list = new ArrayList<>();
    List<UserMeetings> EuserMeetings = new ArrayList<>();
    private int Selected_Event;
    Dialog dialog;

    ArrayList<String> attendee = new ArrayList<>();
   public static ArrayList<String> Selected_Event_attendeeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditEventBinding activityEditEventBinding = ActivityEditEventBinding.inflate(getLayoutInflater());
        ConstraintLayout backgroundLayout = activityEditEventBinding.getRoot();
        int statusBarColor = ((ColorDrawable) backgroundLayout.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(statusBarColor);
        Intent intent = getIntent();
        java.io.Serializable serializableExtra = intent.getSerializableExtra("EVENT_LIST");
        List<?> serializableList = (List<?>) serializableExtra;
        if (serializableList != null) {
            for (Object item : serializableList) {
                if (item instanceof event_model) {
                    event_model event = (event_model) item;
                    event_list.add(event);
                }
            }
        }
        java.io.Serializable serializable_User_List_Extra = intent.getSerializableExtra("UserMeetingList");
        List<?> serializableUserList = (List<?>) serializable_User_List_Extra;
        if (serializableUserList != null) {
            for (Object item : serializableUserList) {
                if (item instanceof UserMeetings) {
                    UserMeetings userMeetings = (UserMeetings) item;
                    EuserMeetings.add(userMeetings);
                }
            }
        }
        Selected_Event = intent.getIntExtra("selected_item",0);
        user = camtrack.cmeet.activities.MainActivity.getuser();
        dialog = new Dialog(this);
        if(event_list.get(Selected_Event).getAttendee() != null)
        { attendee = new ArrayList<>(Arrays.asList(event_list.get(Selected_Event).getAttendee()));}
        else{attendee = new ArrayList<>();}
        activityEditEventBinding.editDescription.setText(event_list.get(Selected_Event).getDescription());
        activityEditEventBinding.editlocation.setText(event_list.get(Selected_Event).getLocation());
        activityEditEventBinding.editSummary.setText(event_list.get(Selected_Event).getTitle());
        activityEditEventBinding.meetingid.setText(event_list.get(Selected_Event).getMeetingId());
            Viewattendeesfragment fragment = new Viewattendeesfragment();
            fragment.cmeet_list = event_list;
            fragment.ClickedItem = Selected_Event;
            fragment.Viewattendees_List_of_User_Meetings = EuserMeetings;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.editattendeesfragment, fragment)
                    .commit();

        activityEditEventBinding.saveEvent.setOnClickListener(v->
        {
            event_list.get(Selected_Event).setTitle(activityEditEventBinding.editSummary.getText().toString());
            event_list.get(Selected_Event).setLocation(activityEditEventBinding.editlocation.getText().toString());
            event_list.get(Selected_Event).setDescription(activityEditEventBinding.editDescription.getText().toString());
            event_list.get(Selected_Event).setAttendee(attendee.toArray(new String[0]));
            Toast.makeText(EditEvent.this, attendee.toString(), Toast.LENGTH_LONG).show();
            Retrofit R = Retrofit_Base_Class.getClient();
            Request_Maker RM = new Request_Maker();
            Dialog delaydialog = cmeet_delay.delaydialogCircular(this);
            RM.meetings(R,event_list.get(Selected_Event), delaydialog, EditEvent.this);
        });
        activityEditEventBinding.newparticipant.setOnClickListener(c->
        {
            Dialog event_dial;
            event_dial = newparticipantDialog();
            event_dial.show();
            TextView newatt  = event_dial.findViewById(R.id.newattendee);

            event_dial.findViewById(R.id.add_email).setOnClickListener(view ->
            {
                if(newatt.getText()  != null )
                {
                    if(attendee.contains(newatt.getText().toString()))
                    {
                        Toast.makeText(this,"Attendee Already Present",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(Patterns.EMAIL_ADDRESS.matcher(newatt.getText().toString()).matches())
                        {attendee.add(newatt.getText().toString());}
                        else {
                            Toast.makeText(this,"Inserez un E-mail valid ",Toast.LENGTH_LONG).show();

                        }
                    }
                    newatt.setText(null);
                }
            });
            event_dial.findViewById(R.id.valid).setOnClickListener(validate->
            {
                event_list.get(Selected_Event).setAttendee(attendee.toArray(new String[0]));
                Viewattendeesfragment Nfragment = new Viewattendeesfragment();
                Nfragment.cmeet_list = event_list;
                Nfragment.ClickedItem = Selected_Event;
                Nfragment.Viewattendees_List_of_User_Meetings = EuserMeetings;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.editattendeesfragment, Nfragment)
                        .commit();
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
        Objects.requireNonNull(dialog.getWindow()).setLayout(5 * (width) / 7, 1618 * (width) / 1000);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
//Implement the server side of update_meetings
//Implement ways to update modifications made