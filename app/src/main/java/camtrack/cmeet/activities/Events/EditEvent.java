package camtrack.cmeet.activities.Events;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityEditEventBinding;
import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;

import java.util.List;

public class EditEvent extends AppCompatActivity
{
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditEventBinding activityEditEventBinding = ActivityEditEventBinding.inflate(getLayoutInflater());
        user = MainActivity.getuser();
        activityEditEventBinding.editDescription.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getDescription());
        activityEditEventBinding.editlocation.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getLocation());
        activityEditEventBinding.editSummary.setText(MainActivity.get_cmeet_event_list().get(ClickedItem).getTitle());
        {
            Viewattendeesfragment fragment = new Viewattendeesfragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.editattendeesfragment, fragment)
                    .commit();
        }
        activityEditEventBinding.clear.setOnClickListener(v-> {finish();});
        setContentView(activityEditEventBinding.getRoot());

    }
}