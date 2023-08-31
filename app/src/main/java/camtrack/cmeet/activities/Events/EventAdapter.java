package camtrack.cmeet.activities.Events;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.calendar.model.Event;

import java.util.List;

import camtrack.cmeet.R;
// Event adapater for google events
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<Event> eventList;
    private final List<event_model> event_modelList;
    private final Context mcontext;
    static  public int adapter_choice;
    static public int ClickedItem;
    View itemView;

    /**
     * event adapter Constructor for google event
     * @param eventList
     * @param context
     */
    public EventAdapter( List<Event> eventList, Context context) {
       this.eventList = eventList;
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
            itemView.setOnClickListener(view -> {
                ClickedItem = getAdapterPosition();
                Intent I = new Intent(mcontext,ViewEvent.class);
                mcontext.startActivity(I);
            });
            // Initialize other views from the item_event layout as needed
        }
    }
}

//making Event View holder static might cause trouble