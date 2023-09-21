package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class Viewattendeesfragment extends Fragment
{
    public MutableLiveData<String> attendee_signed =  new MutableLiveData<>();
    ArrayList<String> attendeeList = null;
    Attendee_Recycler_Adapter attendeeRecyclerAdapter;
    public List<Event> googleEvents = camtrack.cmeet.activities.MainActivity.items();
    public List<event_model> cmeet_list = MainActivity.get_cmeet_event_list();
    Boolean is_list_attendeeList_filled = true;
    String[] a = {"Deea","qzdazd","adazdz","azdzda","adazdad","adazdadaz","azdazdazdaz","azdazdazdzd","zdadazd"};
    Attendee_Recycler_Adapter.ViewHolder viewHolder;
    RecyclerView recyclerView;
    public Viewattendeesfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_attendee_fragment, container, false);
        recyclerView = view.findViewById(R.id.attendees_frag_view);

        int maxHeightInPixels;
        if((cmeet_list.get(ClickedItem).getAttendee()) == null)
        {
            maxHeightInPixels = getResources().getDimensionPixelSize(R.dimen.height_for_3);
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = maxHeightInPixels;
            recyclerView.setLayoutParams(layoutParams);
            is_list_attendeeList_filled = false;
        }
        else if((cmeet_list.get(ClickedItem).getAttendee().length) >= 4)
        {
            maxHeightInPixels = getResources().getDimensionPixelSize(R.dimen.max_height);
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = maxHeightInPixels;
            recyclerView.setLayoutParams(layoutParams);
        }
        if(is_list_attendeeList_filled)
        {
            attendeeList = new ArrayList<>(Arrays.asList(cmeet_list.get(ClickedItem).getAttendee()));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        //Attendee_Recycler_Adapter attendeeRecyclerAdapter = new Attendee_Recycler_Adapter(a,getContext());
        attendeeRecyclerAdapter = new Attendee_Recycler_Adapter(attendeeList,getContext(),this);
        recyclerView.setAdapter(attendeeRecyclerAdapter);
        return view;
    }
    public void update_signature(int attendee_position)
    {
        viewHolder = (Attendee_Recycler_Adapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(attendee_position);
        if (viewHolder != null)
        {
            viewHolder.removeattendee.setVisibility(View.GONE);
            viewHolder.makeowner.setBackground(null);
            viewHolder.makeowner.setImageResource(R.drawable.ic_baseline_check_24);
            viewHolder.makeowner.setVisibility(View.VISIBLE);
        }
    }
    public ArrayList<String> getAttendeeList()
    {
        return attendeeRecyclerAdapter.attendeeList;
    }
    public int getAttendeePosition(String AttendeeId)
    {
        return attendeeRecyclerAdapter.attendeeList.indexOf(AttendeeId);
    }

    public int isViewVisible()
    {
        viewHolder = (Attendee_Recycler_Adapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(0);
        if (viewHolder != null) {
            return viewHolder.makeowner.getVisibility();
        }
        else
        {
            return  22;
        }
    }
}
