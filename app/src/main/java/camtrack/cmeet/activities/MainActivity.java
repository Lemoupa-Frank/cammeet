package camtrack.cmeet.activities;
//  Client ID was used instead of reading json file
// Add the ability to set a pin to login
// Also think about storing credentials in a valid class so that there can be access from needed classes
// Clicking meeting twice at once generate multiple instances of meetings activity
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import camtrack.cmeet.R;
import camtrack.cmeet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    //Load previous events from cache data or store some default event
    public static List<Event> items = null ;
    Events events;
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private GoogleAccountCredential googleAccountCredential;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(activityMainBinding.getRoot());
        progressBar = findViewById(R.id.circularProgressBar);
        // Set up Google Sign-In and Google Account Credential
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR_READONLY))
                .requestIdToken("127612518635-gq1ckmkdplnb4c3tqtrp40nch0epp1n2.apps.googleusercontent.com")
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Create a GoogleAccountCredential using the GoogleSignInAccount
        googleAccountCredential = GoogleAccountCredential.usingOAuth2(
                this, Collections.singleton(CalendarScopes.CALENDAR_READONLY));
        googleAccountCredential.setSelectedAccountName(null); // Ensure we start with no account selected
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
        {
            // User is already signed in, use the account to set up the GoogleAccountCredential
            googleAccountCredential.setSelectedAccount(account.getAccount());
            Toast.makeText(this, googleAccountCredential.getSelectedAccountName()+" signed in", Toast.LENGTH_SHORT).show();
            activityMainBinding.bt2.setOnClickListener(v-> async());
        } else
        {
            // User is not signed in, start the sign-in flow
            Toast.makeText(this, "No User signed in", Toast.LENGTH_SHORT).show();
            activityMainBinding.button.setOnClickListener(
                    v-> signIn()
            );
        }
        // Restricting the onClick to the if means it works only on start of the application
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
            if (account != null)
            {
                googleAccountCredential.setSelectedAccount(account.getAccount());
            }
        } catch (Exception e) {
            Log.d("MainActivity", "handleSignInResult: " + e.getMessage());
            System.out.println("**************Line 109 Mainactivity"+e);
            activityMainBinding.text.setText(e.toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Step 3: Use the Calendar API to access and manage events

    private List<Event> getEventsForDay() {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Calendar service = new Calendar.Builder(transport, jsonFactory, googleAccountCredential)
                .setApplicationName("xsxsxs")
                .build();


        try
        {

            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate startf = today.minusMonths(8);
            java.time.LocalDate starti = today.plusMonths(8);
            Date s = Date.from(startf.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date e = Date.from(starti.atStartOfDay(ZoneId.systemDefault()).toInstant());



             events = service.events().list("primary")
                    .setMaxResults(50)
                    .setTimeMin(new com.google.api.client.util.DateTime(s))
                    .setTimeMax(new com.google.api.client.util.DateTime(e))
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            // Get the list of events from the query result
            System.out.println("------------------"+events);
            items = events.getItems();
            //items.get(0).getOrganizer();
            System.out.println("------------------"+items);
            //items.get(1).getSummary();
            // Process the events as needed (e.g., display them in your app)
        if (items.isEmpty())
        {
            System.out.println("******************No Events ************");
            return null;
        }
        else
        {

            for (Event event : items)
            {
                DateTime start = event.getStart().getDateTime();
                event.get("Attendees");
                if (start == null)
                {
                    start = event.getStart().getDate();
                }
                System.out.println("Summary: "+ event.getSummary() + "   Start: " + start );
            }
            System.out.println("++++++++++++++++ End +++++++++++++++");
        }
        } catch (UserRecoverableAuthIOException e) {
            // Handle user-authorization errors
            startActivityForResult(e.getIntent(), RC_SIGN_IN);
            System.out.println("***************UserRecoverableAuthIOException ***************"+ e);
           //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } // Handle authorization errors
        catch (IOException e) {
            // Handle Google API errors
           //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show().;
            System.out.println("*****************IOException*************"+ e);
        }// Handle other I/O errors
        return items;
    }
    public void async()
    {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() ->
        {
            final List<Event> E = getEventsForDay();
            items = E;
            activityMainBinding.text.post(() -> activityMainBinding.text.setText(E != null ? "Events found" : "No Events"));
            progressBar.post(()->progressBar.setVisibility(View.INVISIBLE));
            if (items == null)
            {
                activityMainBinding.getRoot().post(()->Toast.makeText(MainActivity.this,"Unable to Load events",Toast.LENGTH_LONG).show());
                //inflate Dialog
                //you will have two checks if null get cache values if cache are null then display toast
            }
            else
            {
                Intent I = new Intent(MainActivity.this, camtrack.cmeet.activities.Events.MainActivity.class);
                startActivity(I);
            }
        }).start();
    }
    public static List<Event> items()
    {
        return items;
    }
}