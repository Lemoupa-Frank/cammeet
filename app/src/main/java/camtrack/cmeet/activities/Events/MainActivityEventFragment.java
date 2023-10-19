package camtrack.cmeet.activities.Events;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.calendar.model.Event;

import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class MainActivityEventFragment extends Fragment
{

    public List<event_model> cmeet_list = MainActivity.get_cmeet_event_list();
    public MainActivityEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_model_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.event_model_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        EventAdapter.adapter_choice = 1;
        EventAdapter eventAdapter = new EventAdapter(cmeet_list,getContext(),5);
        recyclerView.setAdapter(eventAdapter);
        return view;
    }


}