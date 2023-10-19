package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class SignatureRecordFragment extends Fragment
{
    RecyclerView recyclerView;
    Signature_record_adapter Signature_adapter;
    ArrayList<String> attendeeList = null;
    Signature_record_adapter.EventViewHolder viewHolder;

    public SignatureRecordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_attendee_fragment, container, false);
        recyclerView = view.findViewById(R.id.attendees_frag_view);

        if(MainActivity.get_cmeet_event_list().get(ClickedItem).getAttendee() != null)
        {
            attendeeList = new ArrayList<>(Arrays.asList(MainActivity.get_cmeet_event_list().get(ClickedItem).getAttendee()));
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        Signature_adapter = new Signature_record_adapter(attendeeList);
        recyclerView.setAdapter(Signature_adapter);
        return view;
    }

    public int getAttendeePosition(String AttendeeId)
    {
        return Signature_adapter.attendeeList.indexOf(AttendeeId);
    }

    public void update_signature(int attendee_position, Bitmap Signature, String  Name)
    {
        viewHolder = (Signature_record_adapter.EventViewHolder) recyclerView.findViewHolderForAdapterPosition(attendee_position);
        if (viewHolder != null)
        {
            viewHolder.attendee_name.setVisibility(View.VISIBLE);
            viewHolder.attendee_name.setText(Name);
            viewHolder.Signature.setImageBitmap(Signature);
        }
    }

}
