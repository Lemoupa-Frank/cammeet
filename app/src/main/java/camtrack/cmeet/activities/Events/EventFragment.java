package camtrack.cmeet.activities.Events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class EventFragment extends Fragment {

    public List<Event> a = MainActivity.items();
    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        EventAdapter eventAdapter = new EventAdapter(a,getContext());
        recyclerView.setAdapter(eventAdapter);
        return view;
    }


}
