package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public Viewattendeesfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_attendee_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.attendees_frag_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        Attendee_Recycler_Adapter attendeeRecyclerAdapter = new Attendee_Recycler_Adapter((cmeet_list.get(ClickedItem).getAttendee()),getContext());
        recyclerView.setAdapter(attendeeRecyclerAdapter);
        return view;
    }
}
