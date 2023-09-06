package camtrack.cmeet.activities;


import static camtrack.cmeet.activities.DatePickerFragment.enddate;
import static camtrack.cmeet.activities.DatePickerFragment.enddatetemp;
import static camtrack.cmeet.activities.DatePickerFragment.startdate;
import static camtrack.cmeet.activities.DatePickerFragment.startdatetemp;
import static camtrack.cmeet.activities.login.data.cache_user.cache_a_user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.Events.MainActivityEventFragment;
import camtrack.cmeet.activities.Events.event_model;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityMainBinding;
import camtrack.cmeet.databinding.DialogBinding;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    //Load previous events from cache data or store some default event
    public static List<event_model> cmeet_event_list; public static List<Event> items = null;
    MutableLiveData<List<Event>> items_listener = new MutableLiveData<>();
   // MutableLiveData<List<event_model>> cmeet_event_list_listener = new MutableLiveData<>();
    Request_Route request_route_instqnce; Retrofit retrofitobj;
    public DialogBinding dialogBinding;
    public TextView starttext, endtext;
    ActivityMainBinding activityMainBinding;
    GoogleSignInAccount account;
    SharedPreferences sharedPreferences;
    public static  String userid;
    Dialog dialog, delaydialog;
    Events events;
    private GoogleSignInClient googleSignInClient;
    private  GoogleAccountCredential googleAccountCredential;
    public static User user;

    public static List<Event> items() {
        return items;
    }
    public static List<event_model> get_cmeet_event_list() {
        return cmeet_event_list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //cmeet_alert.displayAlertDialog(this, sharedPreferences.getString("displayName",""));
        // Set up Google Sign-In and Google Account Credential
        account = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR_READONLY))
                .requestIdToken("127612518635-gq1ckmkdplnb4c3tqtrp40nch0epp1n2.apps.googleusercontent.com")
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Create a GoogleAccountCredential using the GoogleSignInAccount
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR_READONLY));
        // Ensure we start with no account selected
        googleAccountCredential.setSelectedAccountName(null);
        // Resposibles of starting the authorieation flow to get token

        SignIn_Handler();

        // Retrieving user stored in the cache each time this activity is created
        {
            sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
            user =  cache_a_user(null,user,sharedPreferences);
        }


        //Initializing variables
        {
            dialog = new Dialog(this);
            activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
            dialogBinding = DialogBinding.inflate(getLayoutInflater());
            delaydialog = cmeet_delay.delaydialogCircular(this);
        }



        items_listener.observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                {
                    if(events != null)
                    {
                        {
                            retrofitobj = Retrofit_Base_Class.getClient();
                            Request_Maker request_maker = new Request_Maker();
                            cmeet_event_list = cmeet_from_googleEvent(items, MainActivity.this);
                            request_maker.store_todays_meets(retrofitobj,MainActivity.this,cmeet_event_list);
                        }
                        MainActivityEventFragment fragment = new MainActivityEventFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.MainActivityFrag, fragment)
                                .commit();
                    }
                }
            }
        });

        async();

        setContentView(activityMainBinding.getRoot());
        activityMainBinding.bt2.setOnClickListener(v -> async());
        activityMainBinding.button.setOnClickListener(v ->
        {
            Dialog event_dial;
            event_dial = openDialog(this);
            if (items != null)
            {
                MainActivityEventFragment fragment = new MainActivityEventFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.MainActivityFrag, fragment)
                        .commit();
            }
            event_dial.show();
        });




    }

    // Handles the result of the google signactivity started by signin()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();
            if (account != null) {
                googleAccountCredential.setSelectedAccount(account.getAccount());
            }
        } catch (Exception e) {
            Log.d("MainActivity", "handleSignInResult: " + e.getMessage());
            System.out.println("**************Line 139 Mainactivity" + e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private List<Event> getEventsForDay(DateTime Start, DateTime end) {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Calendar service = new Calendar.Builder(transport, jsonFactory, googleAccountCredential)
                .setApplicationName("cammeet")
                .build();


        try {
            events = service.events().list("primary")
                    .setMaxResults(50)
                    .setTimeMin(Start)
                    .setTimeMax(end)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            // Get the list of events from the query result
            System.out.println("------------------" + events);
            items = events.getItems();
            //items.get(0).getOrganizer();
            System.out.println("------------------" + items);

            if (items.isEmpty()) {
                System.out.println("******************No Events ************");
                return null;
            } else {

                for (Event event : items) {
                    DateTime start = event.getStart().getDateTime();
                    event.get("Attendees");
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    System.out.println("Summary: " + event.getSummary() + "   Start: " + start);
                }
                System.out.println("++++++++++++++++ End +++++++++++++++");
            }
        } catch (UserRecoverableAuthIOException e) {
            // Handle user-authorization errors
            startActivityForResult(e.getIntent(), RC_SIGN_IN);
            System.out.println("***************UserRecoverableAuthIOException ***************" + e);
            //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } // Handle authorization errors
        catch (IOException e) {
            // Handle Google API errors
            //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show().;
            System.out.println("*****************IOException************* line 174 " + e);
        }// Handle other I/O errors
        return items;
    }

    /**
     * A function that maes an asynchronous call to get events
     * the function sets a progressbar before creating the
     * new thread
     */
    public void async()
    {
        new Thread(() ->
        {
            items = getEventsForDay(startdate, enddate);
            items_listener.postValue(items);
            if (items == null)
            {
                activityMainBinding.getRoot().post(() -> Toast.makeText(MainActivity.this, "Unable to Load events", Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    /**
     * Check if there is a google account signed in if there  is none
     * it starts a signin flow
     */
    public void SignIn_Handler() {
        if (account != null) {
            // User is already signed in, use the account to set up the GoogleAccountCredential
            googleAccountCredential.setSelectedAccount(account.getAccount());
            userid = account.getEmail();
        } else {
            // User is not signed in, start the sign-in flow
            Toast.makeText(this, "No User signed in", Toast.LENGTH_SHORT).show();
            signIn();

        }
    }

    /**
     * This changes the google account which is signed in
     * and logs the user out
     */
    protected void change_account() {

    }

    public Dialog openDialog(Context con) {
        dialog.setContentView(R.layout.dialog);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        dialog.getWindow().setLayout(5 * (width) / 7, 5 * (height) / 7);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void StartDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        starttext = dialog.findViewById(R.id.textstartdate);
        newFragment.setListener((year, month, dayOfMonth) -> {
            // Update the TextView with the selected date
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, dayOfMonth);
            DateTime dateTime = DateTime.parseRfc3339(selectedDate);
            starttext.setText(selectedDate);
            startdatetemp = dateTime;
        });
        newFragment.show(getSupportFragmentManager(), "StartDate");
    }

    public void EndDatePickerDialog(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        endtext = dialog.findViewById(R.id.textendtdate);
        newFragment.setListener((year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, dayOfMonth);
            DateTime dateTime = DateTime.parseRfc3339(selectedDate);
            endtext.setText(selectedDate);
            enddatetemp = dateTime;
        });
        newFragment.show(getSupportFragmentManager(), "EndDate");
    }

    public void dialog_canel(View view)
    {
        dialog.cancel();
    }
    public void dialog_validate(View view)
    {
        if(startdatetemp != null && enddatetemp != null)
        {
            startdate = startdatetemp;
            enddate = enddatetemp;
            dialog.cancel();
        }
        else
        {
            Toast.makeText(this, googleAccountCredential.getSelectedAccountName() + " signed in", Toast.LENGTH_SHORT).show();
        }
    }

    public List<event_model> cmeet_from_googleEvent(List<Event> myevent, Context con)
    {

        List<event_model> LEM = new ArrayList<>();
        for (Event ev : myevent)
        {
            event_model cm = new event_model();
            cm.setMeetingId(ev.getId());
            cm.setLocation(ev.getLocation());
            cm.setNumberOfParticipants(ev.getAttendees()==null?0:ev.getAttendees().size());
            cm.setOwner(ev.getOrganizer().getEmail()==null?ev.getCreator().getId():ev.getOrganizer().getEmail());
            cm.setDateofcreation(ev.getCreated().toString());
            cm.setStartdate(ev.getStart().getDate()==null?"none":ev.getStart().getDate().toString());
            cm.setEnddate(ev.getEnd().getDate()==null?"none":ev.getEnd().toString());
            cm.setDescription(ev.getDescription()==null?"none":ev.getDescription());
            cm.setTitle(ev.getSummary()==null?"none":ev.getSummary());
            cm.setAttendee(getAttendees(ev.getAttendees()));
            cm.setuserid(MainActivity.userid==null?user.getUserId():MainActivity.userid);
            LEM.add(cm);
        }
        cmeet_event_list = LEM;
        return LEM;
    }
    public static String[] getAttendees(List<EventAttendee> attendeesList)
    {
        ArrayList<String> Attendees = new ArrayList<>();
        if (attendeesList == null)
        {
            return null;
        }
        for (EventAttendee attendee : attendeesList)
        {
            Attendees.add(attendee.getEmail());
        }
        return Attendees.toArray(new String[0]);
    }

    public static User getuser()
    {
        return user;
    }
}