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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private final List<Event> eventList;
    private final Context mcontext;
    static public int ClickedItem;
    View itemView;

    //eventList = MainActivity
    public EventAdapter(List<Event> eventList, Context context) {
       this.eventList = eventList;
       this.mcontext = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         this.itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textEventSummary.setText(event.getSummary());
        // Bind other event data to views
    }

    @Override
    public int getItemCount() {
        return eventList.size();
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
                System.out.println("**-*-*-*-*-*-*-*-*-*--clickes clicky clicka:" + eventList.get(getAdapterPosition()).getSummary() );
                ClickedItem = getAdapterPosition();
                Intent I = new Intent(mcontext,ViewEvent.class);
                mcontext.startActivity(I);
            });
            // Initialize other views from the item_event layout as needed
        }
    }
}

//making Event View holder static might cause trouble