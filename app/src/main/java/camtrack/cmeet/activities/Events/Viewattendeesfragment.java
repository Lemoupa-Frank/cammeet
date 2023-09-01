package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.Arrays;
import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class Viewattendeesfragment extends Fragment
{
    public List<Event> googleEvents = camtrack.cmeet.activities.MainActivity.items();
    public List<event_model> cmeet_list = MainActivity.get_cmeet_event_list();
    String[] a = {"Deea","qzdazd","adazdz","azdzda","adazdad","adazdadaz","azdazdazdaz","azdazdazdzd","zdadazd"};
    public Viewattendeesfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_attendee_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.attendees_frag_view);

        int maxHeightInPixels;
        if((cmeet_list.get(ClickedItem).getAttendee()) == null)
        {
            maxHeightInPixels = getResources().getDimensionPixelSize(R.dimen.height_for_3);
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = maxHeightInPixels;
            recyclerView.setLayoutParams(layoutParams);
        }
        else if((cmeet_list.get(ClickedItem).getAttendee().length) >= 4)
        {
            maxHeightInPixels = getResources().getDimensionPixelSize(R.dimen.max_height);
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = maxHeightInPixels;
            recyclerView.setLayoutParams(layoutParams);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        //Attendee_Recycler_Adapter attendeeRecyclerAdapter = new Attendee_Recycler_Adapter(a,getContext());
        Attendee_Recycler_Adapter attendeeRecyclerAdapter = new Attendee_Recycler_Adapter((cmeet_list.get(ClickedItem).getAttendee()),getContext());
        recyclerView.setAdapter(attendeeRecyclerAdapter);
        return view;
    }
}
