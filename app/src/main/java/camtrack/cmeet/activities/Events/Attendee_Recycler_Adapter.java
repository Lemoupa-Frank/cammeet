package camtrack.cmeet.activities.Events;



import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;
import static camtrack.cmeet.activities.Events.ViewEvent.LUM;
import static camtrack.cmeet.activities.MainActivity.cmeet_event_list;
import static camtrack.cmeet.activities.MainActivity.getuser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;

public class Attendee_Recycler_Adapter extends RecyclerView.Adapter<Attendee_Recycler_Adapter.ViewHolder>
{

    private final ArrayList<String> attendeeList;
    private final Context context;
    int SeletectedAttendee;
    View itemView;

    public Attendee_Recycler_Adapter(ArrayList<String> attendeeList, Context context) {
        this.attendeeList = attendeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        this.itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String attendee_email = attendeeList.get(position);
        if (attendee_email == null)
        {
            holder.attendeeTextView.setText("attendee_email");
        }
        holder.attendeeTextView.setText(attendee_email);
    }

    @Override
    public int getItemCount() {
        if (attendeeList==null)
        {
            return 0;
        }
        return attendeeList.size();
    }

    public void removeItem(int position)
    {
        attendeeList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView attendeeTextView; ImageView makeowner, removeattendee;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            attendeeTextView = itemView.findViewById(R.id.attendee_email);
            makeowner = itemView.findViewById(R.id.attendee_image);
            removeattendee = itemView.findViewById(R.id.Delete_attendee);
            if(getuser().getUserId().equals(cmeet_event_list.get(ClickedItem).getOwner()))
            {
                makeowner.setVisibility(View.VISIBLE);
                removeattendee.setVisibility(View.VISIBLE);
            }
            attendeeTextView.setOnClickListener(view -> {
                SeletectedAttendee = getAdapterPosition();
                // create a dialog in Attendee recycler and access it with its methods
            });

            {
                makeowner.setOnClickListener(v ->
                {
                    SeletectedAttendee = getAdapterPosition();

                    if (LUM != null)
                    {
                        HashMap<String, UserMeetings> UserMeetingsHashMap = new HashMap<>();
                        Request_Maker RM = new Request_Maker();
                        UserMeetingsHashMap = RM.convertAttendeesToHashMap(LUM);
                        if (UserMeetingsHashMap.containsKey(cmeet_event_list.get(ClickedItem).getAttendee()[SeletectedAttendee])) {
                            makeowner.setImageResource(R.drawable.circular_progress_indicator);
                            Toast.makeText(v.getContext(), cmeet_event_list.get(ClickedItem).getAttendee()[SeletectedAttendee] + "is now an owner", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(v.getContext(), cmeet_event_list.get(ClickedItem).getAttendee()[SeletectedAttendee] + "Canot be made owner", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(v.getContext(), "Please Check Your Network", Toast.LENGTH_LONG).show();
                    }
                });
            }
            removeattendee.setOnClickListener(v->
            {

                removeItem(getAdapterPosition());
                EditEvent.Selected_Event_attendeeList =  attendeeList;
            });

            attendeeTextView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });

        }
    }
}

/*
 Thread attendeeThread = new Thread(() -> {
    try {
        getAttendees.execute();
    } catch (IOException e) {
        e.printStackTrace();
    }
}, "AttendeeThread");

attendeeThread.start();*/

// convert attendeesArray to an arralist2