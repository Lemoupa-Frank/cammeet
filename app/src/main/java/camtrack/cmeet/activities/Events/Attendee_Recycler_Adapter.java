package camtrack.cmeet.activities.Events;


import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import camtrack.cmeet.R;

public class Attendee_Recycler_Adapter extends RecyclerView.Adapter<Attendee_Recycler_Adapter.ViewHolder> {

    private final String[] attendeeArray;
    private final Context context;
    int SeletectedAttendee;
    View itemView;

    public Attendee_Recycler_Adapter(String[] attendeeArray, Context context) {
        this.attendeeArray = attendeeArray;
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
        String attendee_email = attendeeArray[position];
        if (attendee_email == null)
        {
            holder.attendeeTextView.setText("attendee_email");
        }
        holder.attendeeTextView.setText(attendee_email);
    }

    @Override
    public int getItemCount() {
        if (attendeeArray==null)
        {
            return 0;
        }
        return attendeeArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView attendeeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            attendeeTextView = itemView.findViewById(R.id.attendee_email);
            itemView.setOnClickListener(view -> {
                SeletectedAttendee = getAdapterPosition();
                Intent I = new Intent(context,ViewEvent.class);
                context.startActivity(I);
            });
        }
    }
}