package camtrack.cmeet.activities.Events;



import static camtrack.cmeet.activities.MainActivity.getuser;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Attendee_Recycler_Adapter extends RecyclerView.Adapter<Attendee_Recycler_Adapter.ViewHolder>
{
    HashMap<String, UserMeetings> UserMeetingsHashMap = new HashMap<>();
    public ArrayList<String> attendeeList;
    private final Context context;

    public List<event_model> cmeet_list;
    public int ClickedItem;

    public List<UserMeetings> List_of_User_Meetings;

    LifecycleOwner lifecycleOwner;
    int SeletectedAttendee;
    View itemView;

    public Attendee_Recycler_Adapter(ArrayList<String> attendeeList, Context context,LifecycleOwner lifecycleOwner) {
        this.attendeeList = attendeeList;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        if (List_of_User_Meetings != null)
        {
            Request_Maker RM = new Request_Maker();
            UserMeetingsHashMap = RM.convertAttendeesToHashMap(List_of_User_Meetings);
        }
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
        if( UserMeetingsHashMap != null)
        {
            if (UserMeetingsHashMap.containsKey(attendee_email))
            {
                if (Objects.requireNonNull(UserMeetingsHashMap.get(attendee_email)).getRole().equals("owner"))
                {
                    holder.makeowner.setImageResource(R.drawable.circular_progress_indicator);
                }
            }
        }
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

            if(getuser().getUserId().equals(cmeet_list.get(ClickedItem).getOwner()))
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

                    if (List_of_User_Meetings != null)
                    {
                        Request_Maker RM = new Request_Maker();
                        UserMeetingsHashMap = RM.convertAttendeesToHashMap(List_of_User_Meetings);
                        if (UserMeetingsHashMap.containsKey(cmeet_list.get(ClickedItem).getAttendee()[SeletectedAttendee]))
                        {
                            makeowner.setImageResource(R.drawable.circular_progress_indicator);
                            Toast.makeText(v.getContext(), cmeet_list.get(ClickedItem).getAttendee()[SeletectedAttendee] + "is now an owner", Toast.LENGTH_LONG).show();
                        } else
                        {
                            Toast.makeText(v.getContext(), cmeet_list.get(ClickedItem).getAttendee()[SeletectedAttendee] + "Canot be made owner", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), "Please Check Your Network", Toast.LENGTH_LONG).show();
                        Retrofit retroObj = Retrofit_Base_Class.getClient();
                        Request_Maker RM = new Request_Maker();
                        getAttendees(retroObj,cmeet_list.get(ClickedItem).getMeetingId(),itemView.getContext());
                    }
                });
            }
            removeattendee.setOnClickListener(v->
            {

                removeItem(getAdapterPosition());
                EditEvent.Selected_Event_attendeeList =  attendeeList;
            });

            attendeeTextView.setOnLongClickListener(view -> false);


        }
    }

    /**
     * Retrofit call to get the list of users and their roles in selected meeting
     * Onsucess modifies list of users the static variable that stores the list
     * @param retrofitObject Retrofit object already instantiated
     * @param meetsId The id of the meeting you wish to get the Users
     */

    public void getAttendees(Retrofit retrofitObject, String meetsId, Context con)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<List<UserMeetings>> _getAttendees = RR.get_attendees(meetsId);
        _getAttendees.enqueue(new Callback<List<UserMeetings>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserMeetings>> call, @NonNull Response<List<UserMeetings>> response) {
                if(response.isSuccessful())
                {
                    List_of_User_Meetings = response.body();
                    Toast.makeText(con,"Success",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(con,"Something Went wrong",Toast.LENGTH_LONG).show();
                    System.out.println("********************************************************");
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserMeetings>> call, @NonNull Throwable t) {
                {
                    Toast.makeText(con,t.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
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

/*
            if(LUM.get(getAdapterPosition()).getRole().equals("owner"))
            {
                makeowner.setVisibility(View.VISIBLE);
                removeattendee.setVisibility(View.VISIBLE);
            }
 */
//Try to always set LUM to null on destroy