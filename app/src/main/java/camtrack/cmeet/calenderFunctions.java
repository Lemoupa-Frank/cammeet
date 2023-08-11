package camtrack.cmeet;
import android.util.Log;


import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

public class calenderFunctions {
    // Make sure googleAccountCredential is set with the user's account
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    NetHttpTransport HTTP_TRANSPORT;

    {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            Log.e("calendar Function line 30", "*****************: " + e.getMessage());
        }
    }

    public void getEventsForDay( GoogleAccountCredential googleAccountCredential) {
        // Make sure googleAccountCredential is set with the user's account

        // Create a Calendar instance using the googleAccountCredential
        Calendar calendarService = new Calendar.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                googleAccountCredential)
                .setApplicationName(String.valueOf(R.string.app_name))
                .build();

        try {
            // Define the start and end of the day you want to retrieve events for
            Date startOfDay = null  /* Set the start of the day */;
            Date endOfDay  = null /* Set the end of the day */;
            DateTime now = new DateTime(System.currentTimeMillis());
            // Create a query to retrieve events for the specified time range
            Events events = calendarService.events()
                    .list("primary")
                    .setMaxResults(10)// .setTimeMin(new DateTime(startOfDay))
                    .setTimeMin(now) //.setTimeMax(new DateTime(endOfDay))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            // Get the list of events from the query result
            List<Event> items = events.getItems();

            // Process the events as needed (e.g., display them in your app)
            for (Event event : items) {
                String eventName = event.getSummary();
                String eventLocation = event.getLocation();
                // You can retrieve other event details as well
                // ...
            }
        } catch (IOException e) {
            Log.e("calendar Function line 70", "*****************: " + e.getMessage());
        }
    }
}
