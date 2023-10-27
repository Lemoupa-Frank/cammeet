package camtrack.cmeet.activities.Events;




import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// Event adapater for google events
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<Event> eventList;
    private final List<event_model> event_modelList;
    Intent I;
    List<UserMeetings> userMeetingsList;
    private final Context mcontext;
    static  public int adapter_choice;
    public int ClickedItem;
    View itemView;

    /**
     * event adapter Constructor for google event
     * @param eventList List of google events
     * @param context The context of the calling class
     */
    public EventAdapter( List<Event> eventList, Context context) {
       this.eventList = null;
       this.event_modelList = null;
       this.mcontext = context;
    }

    /**
     * event adapter constructor for cmeet database event
     * @param eventList list of cmeet events
     * @param context Context of calling class
     * @param start this means it is an adapter for event_model
     */
    public EventAdapter(List<event_model> eventList, Context context, int start) {
        this.event_modelList = eventList;
        this.mcontext = context;
        this.eventList = null;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         this.itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position)
    {
        if (eventList != null && adapter_choice == 0) {
            Event event = eventList.get(position);
            holder.textEventSummary.setText(event.getSummary());
        }
        else if (event_modelList != null && adapter_choice == 1)
        {
            event_model ev = event_modelList.get(position);
            holder.textEventSummary.setText(ev.getTitle());
        }
        // Bind other event data to views
    }

    @Override
    public int getItemCount()
    {
        if (eventList != null && adapter_choice == 0) {
            return eventList.size();
        }
        else if (event_modelList != null && adapter_choice == 1)
        {
            return event_modelList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }




     public class EventViewHolder extends RecyclerView.ViewHolder{
        TextView textEventSummary;

        // Add other views from the item_event layout as needed

        EventViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textEventSummary = itemView.findViewById(R.id.textEventSummary);
            textEventSummary.setOnClickListener(view -> {
                Retrofit retroObj = Retrofit_Base_Class.getClient();
                Request_Maker RM = new Request_Maker();
                ClickedItem = getAdapterPosition();
                getAttendees(retroObj,event_modelList.get(ClickedItem).getMeetingId(),itemView.getContext());

            });
            textEventSummary.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied Text", event_modelList.get(ClickedItem).getMeetingId());
                    Vibrator vibrator = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    Toast.makeText(view.getContext(), "L'indentifiant Copier",Toast.LENGTH_LONG).show();
                    if (vibrator != null && vibrator.hasVibrator()) {
                        vibrator.vibrate(100);}
                    clipboard.setPrimaryClip(clip);

                    return true;
                }
            });
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
        Dialog delayd  = cmeet_delay.delaydialogCircular(con);
        delayd.show();
        _getAttendees.enqueue(new Callback<List<UserMeetings>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserMeetings>> call, @NonNull Response<List<UserMeetings>> response) {
                delayd.cancel();
                if(response.isSuccessful())
                {
                    userMeetingsList = response.body();
                    I = new Intent(con,ViewEvent.class);
                    I.putExtra("EVENT_LIST", new ArrayList<>(event_modelList));
                    I.putExtra("selected_item", ClickedItem);
                    I.putExtra("UserMeetingList", new ArrayList<>(userMeetingsList));
                    con.startActivity(I);
                    Toast.makeText(con,"Success",Toast.LENGTH_LONG).show();
                }
                else
                {
                    I = new Intent(con,ViewEvent.class);
                    I.putExtra("EVENT_LIST", new ArrayList<>(event_modelList));
                    I.putExtra("selected_item", ClickedItem);
                    con.startActivity(I);
                    Toast.makeText(con,"Something Went wrong",Toast.LENGTH_LONG).show();
                    System.out.println("********************************************************");
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserMeetings>> call, @NonNull Throwable t) {
                {
                    delayd.cancel();
                    I = new Intent(con,ViewEvent.class);
                    I.putExtra("EVENT_LIST", new ArrayList<>(event_modelList));
                    I.putExtra("selected_item", ClickedItem);
                    Toast.makeText(con,t.toString(),Toast.LENGTH_LONG).show();
                    con.startActivity(I);
                }
            }
        });
    }
}

//making Event View holder static might cause trouble