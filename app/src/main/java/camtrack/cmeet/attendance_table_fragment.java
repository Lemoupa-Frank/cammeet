package camtrack.cmeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import camtrack.cmeet.activities.Events.ViewEvent;
import camtrack.cmeet.activities.MainActivity;

public class attendance_table_fragment extends Fragment {

    public TableLayout tableLayout;

    TableRow row;

    public int numberofaatendees; public String[] attendee_names; public String[] attendee_email;
    public String[] role; public List<byte[]> attendee_signature;

    public attendance_table_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        tableLayout = rootView.findViewById(R.id.tableLayout);
        addRowsToTable();
        return rootView;
    }

    public void addRowsToTable() {



// Get the display metrics from the parent activity
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int dividedWidth = 250;
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int screenWidth = displayMetrics.widthPixels;

            // Divide the screen width by three and ensure the result is an integer
            dividedWidth = screenWidth / 4;
        }


        for (int i = 0; i< numberofaatendees; i++) {
            row = new TableRow(requireContext());
            TextView columnName = new TextView(requireContext());

            columnName.setText(attendee_names[i]);

            TextView attendee_text_holder = new TextView(requireContext());

            attendee_text_holder.setText(attendee_email[i]);

            TextView attendee_role = new TextView(requireContext());

            attendee_role.setText(role[i]);

            attendee_text_holder.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));
            columnName.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));
            attendee_role.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));


            attendee_text_holder.setBackgroundResource(R.drawable.table_divider);
            attendee_text_holder.setTextColor(getResources().getColor(R.color.white_black, requireContext().getTheme()));
            columnName.setBackgroundResource(R.drawable.table_divider);
            columnName.setTextColor(getResources().getColor(R.color.white_black, requireContext().getTheme()));
            attendee_role.setBackgroundResource(R.drawable.table_divider);
            attendee_role.setTextColor(getResources().getColor(R.color.white_black, requireContext().getTheme()));


            // fouth column start
            ImageView columnImageView = new ImageView(requireContext());
            if(!attendee_signature.isEmpty())
            {
                if(attendee_signature.get(i) != null)
                {
                    columnImageView.setImageBitmap(BitmapFactory.decodeByteArray(attendee_signature.get(i), 0, attendee_signature.get(i).length));

                }
            }

            columnImageView.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));
            columnImageView.setBackgroundResource(R.drawable.table_divider);
            columnImageView.setBackgroundColor(getResources().getColor(R.color.white_black, requireContext().getTheme()));
            // fourth column end

            row.addView(columnName);
            row.addView(attendee_text_holder);
            row.addView(attendee_role);
            row.addView(columnImageView);

            tableLayout.addView(row);
        }
    }

    public void setNumberofaatendees(int numberofaatendees) {
        this.numberofaatendees = numberofaatendees;
    }

    public void setAttendee_names(String[] attendee_names) {
        this.attendee_names = attendee_names;
    }

    public void setAttendee_email(String[] attendee_email) {
        this.attendee_email = attendee_email;
    }

    public void setRole(String[] role) {
        this.role = role;
    }

    public void setAttendee_signature(List<byte[]> attendee_signature) {
        this.attendee_signature = attendee_signature;
    }
}