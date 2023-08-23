package camtrack.cmeet.activities;


import static camtrack.cmeet.activities.DatePickerFragment.enddate;
import static camtrack.cmeet.activities.DatePickerFragment.enddatetemp;
import static camtrack.cmeet.activities.DatePickerFragment.startdate;
import static camtrack.cmeet.activities.DatePickerFragment.startdatetemp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import camtrack.cmeet.R;
import camtrack.cmeet.databinding.ActivityMainBinding;
import camtrack.cmeet.databinding.DialogBinding;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    //Load previous events from cache data or store some default event
    public static List<Event> items = null;
    public DialogBinding dialogBinding;
    public TextView starttext, endtext;
    ActivityMainBinding activityMainBinding;
    GoogleSignInAccount account;


    Dialog dialog;
    Events events;
    private GoogleSignInClient googleSignInClient;
    private GoogleAccountCredential googleAccountCredential;
    private ProgressBar progressBar;

    public static List<Event> items() {
        return items;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(this);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        dialogBinding = DialogBinding.inflate(getLayoutInflater());


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(activityMainBinding.getRoot());
        progressBar = findViewById(R.id.circularProgressBar);

        account = GoogleSignIn.getLastSignedInAccount(this);
        // Set up Google Sign-In and Google Account Credential
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR_READONLY))
                .requestIdToken("127612518635-gq1ckmkdplnb4c3tqtrp40nch0epp1n2.apps.googleusercontent.com")
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);


        activityMainBinding.bt2.setOnClickListener(v -> async());
        activityMainBinding.button.setOnClickListener(v -> {
            Dialog event_dial;
            event_dial = openDialog(this);
            event_dial.show();
        });

        // Create a GoogleAccountCredential using the GoogleSignInAccount
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(CalendarScopes.CALENDAR_READONLY));
        googleAccountCredential.setSelectedAccountName(null); // Ensure we start with no account selected
        SignIn_Handler();
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
            System.out.println("**************Line 96 Mainactivity" + e);
            activityMainBinding.text.setText(e.toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Step 3: Use the Calendar API to access and manage events

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
            //items.get(1).getSummary();
            // Process the events as needed (e.g., display them in your app)
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
    public void async() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() ->
        {
            final List<Event> E = getEventsForDay(startdate, enddate);
            items = E;
            activityMainBinding.text.post(() -> activityMainBinding.text.setText(E != null ? "Events found" : "No Events"));
            progressBar.post(() -> progressBar.setVisibility(View.INVISIBLE));
            if (items == null) {
                activityMainBinding.getRoot().post(() -> Toast.makeText(MainActivity.this, "Unable to Load events", Toast.LENGTH_LONG).show());
                //inflate Dialog
                //you will have two checks if null get cache values if cache are null then display toast
            } else {
                Intent I = new Intent(MainActivity.this, camtrack.cmeet.activities.Events.MainActivity.class);
                startActivity(I);
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
            Toast.makeText(this, googleAccountCredential.getSelectedAccountName() + " signed in", Toast.LENGTH_SHORT).show();
        } else {
            // User is not signed in, start the sign-in flow
            Toast.makeText(this, "No User signed in", Toast.LENGTH_SHORT).show();
            signIn();
            //activityMainBinding.button.setOnClickListener(v-> signIn());
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

}