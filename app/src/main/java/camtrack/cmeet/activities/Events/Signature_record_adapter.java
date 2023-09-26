package camtrack.cmeet.activities.Events;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class Signature_record_adapter extends RecyclerView.Adapter<Signature_record_adapter.EventViewHolder>{
    View itemView;
    public ArrayList<String> attendeeList;

    public Signature_record_adapter(ArrayList<String> attendeeList)
    {
        this.attendeeList = attendeeList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_record_item, parent, false);
        return new Signature_record_adapter.EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        String attendee_email = attendeeList.get(position);
        holder.AttendeeEmail.setText(attendee_email);
    }

    @Override
    public int getItemCount()
    {
        if (attendeeList==null)
        {
            return 0;
        }
        return attendeeList.size();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView AttendeeEmail; ImageView Signature; TextView attendee_name;
        public EventViewHolder(View itemView) {
            super(itemView);
            AttendeeEmail = itemView.findViewById(R.id.attendee_email);
            Signature = itemView.findViewById(R.id.signature);
            attendee_name = itemView.findViewById(R.id.attendee_Name);
        }
    }
}
