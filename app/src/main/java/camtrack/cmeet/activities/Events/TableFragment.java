package camtrack.cmeet.activities.Events;



import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.MainActivity;

public class TableFragment extends Fragment {

    public TableLayout tableLayout;

    public int Clicked_Item;
    


    public TableFragment() {
        // Required empty public constructor
    }

    TableRow row;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        tableLayout = rootView.findViewById(R.id.tableLayout);
        addRowsToTable();
        return rootView;
    }

    private void addRowsToTable() {
        // Sample data for columns
        String[] Attendee = MainActivity.cmeet_event_list.get(Clicked_Item).getAttendee();

        int columnThreeImageResource = R.drawable.ic_launcher_foreground;
        int Number_Rows = MainActivity.cmeet_event_list.get(Clicked_Item).getAttendee() == null ? 0 : MainActivity.cmeet_event_list.get(Clicked_Item).getAttendee().length;
        // Add rows dynamically to the table based on your logic

// Get the display metrics from the parent activity
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int dividedWidth = 250;
        if (getActivity() != null) {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Retrieve the screen width and height
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            // Divide the screen width by three and ensure the result is an integer
             dividedWidth = screenWidth / 3;
        }


        for (int i = 0; i<Number_Rows; i++) {
            row = new TableRow(requireContext());
            TextView columnName = new TextView(requireContext());

            columnName.setText(extractNameFromEmail(Attendee[i]));

            columnName.setOnClickListener(v -> {

            });

            TextView attendee_text_holder = new TextView(requireContext());

            attendee_text_holder.setText(Attendee[i]);

            attendee_text_holder.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));
            columnName.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));


            attendee_text_holder.setBackgroundResource(R.drawable.table_divider);
            columnName.setBackgroundResource(R.drawable.table_divider);

                row.addView(columnName);
                row.addView(attendee_text_holder);


            // Add column 3 (ImageView)
            ImageView columnImageView = new ImageView(requireContext());
            columnImageView.setImageResource(columnThreeImageResource);
            // Customize the ImageView as needed (e.g., dimensions, scale type, etc.)
            // ...

            // Set the OnClickListener on the ImageView
            columnImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the column 3 (ImageView) click event here
                    //handleColumnClick(2); // Assuming column 3 has index 2
                    ViewEvent.observe_signature_click.postValue("0");
                }
            });

            // Add the ImageView to the row

            columnImageView.setLayoutParams(new TableRow.LayoutParams(dividedWidth, 150));
            columnImageView.setBackgroundResource(R.drawable.table_divider);

            row.addView(columnImageView);

            tableLayout.addView(row);
        }
    }

    public void changeColumnImage(int rowIndex,byte[] bitmap)
    {
        row = (TableRow) tableLayout.getChildAt(rowIndex + 1); // Get the desired row
        Toast.makeText(requireContext(), "changeColumncallde", Toast.LENGTH_LONG).show();
        if (row != null) {
            View columnView = row.getChildAt(2); // Get the desired column view
            ImageView columnImageView = (ImageView) columnView; // Cast the column view to ImageView
            columnImageView.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
        }
        else
        {
            Toast.makeText(getContext(), "Participant Not Found", Toast.LENGTH_SHORT).show();
        }
    }
    
    public static String extractNameFromEmail(String email) {
        String[] parts = email.split("@"); // Split email address into username and domain
        String username = parts[0];
        String cleanUsername = username.replaceAll("[^a-zA-Z\\s]", " "); // Remove special characters and numbers
        String cleanedName = cleanUsername.trim(); // Remove leading/trailing whitespace

        // Capitalize each word
        StringBuilder capitalizedName = new StringBuilder();
        for (String word : cleanedName.split("\\s")) {
            if (!word.isEmpty()) {
                capitalizedName.append(Character.toUpperCase(word.charAt(0)));
                capitalizedName.append(word.substring(1).toLowerCase());
                capitalizedName.append(" ");
            }
        }

        return capitalizedName.toString().trim();
    }


}
/*
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <size android:height="1dp" />
    <solid android:color="@color/divider_color" />
</shape>

<TableLayout
    android:id="@+id/tableLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:showDividers="middle">

    tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
tableLayout.setDividerPadding(getResources().getDimensionPixelSize(R.dimen.divider_padding));
 */
//MR Oliver wants to see Livia