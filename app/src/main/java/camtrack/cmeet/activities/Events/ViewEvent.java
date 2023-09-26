package camtrack.cmeet.activities.Events;

import static camtrack.cmeet.activities.Events.EventAdapter.ClickedItem;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.ByteArrayOutputStream;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import camtrack.cmeet.R;
import camtrack.cmeet.Request_Maker;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.UserMeetings.UserMeetingsPK;
import camtrack.cmeet.activities._Dialog;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.customview.SignatureView;
import camtrack.cmeet.databinding.ActivityViewEventBinding;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import camtrack.cmeet.websocket.Message;
import camtrack.cmeet.websocket.webSocketClient;
import retrofit2.Retrofit;
// if the textviews are wrap content then if i set the picture and text only at runtime them i might achieve vanishing effect
// Any conversion to String must be checked for nul  pointers

public class ViewEvent extends AppCompatActivity {
    ActivityViewEventBinding viewEventBinding;


    int Selected_Event; Retrofit retrofitobj;
    public List<Event> a = MainActivity.items();

    static public MutableLiveData<String> observe_signature_click;

    static Observer<String> observer_signature;
    static public List<UserMeetings> LUM; // A network call is done once an event is selected to modify LUM
    // Make LUM mutable, so you can Create viewevent fragment and if LUM loads and is non null recreate it
    public List<event_model> event_List = MainActivity.get_cmeet_event_list();
    private webSocketClient WebSocketClients;
    boolean Signable;
    Message startSign;
    event_model eventmodel;
    URI serverUri = null;
    boolean event_owner = false;
    TableFragment tableFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Start  a process to get user's Meeting, this is what you will use to set
        //Owners in the Attendee Recycler fragment will be determin by this
        // You can put a mutable boolean to create fragment once you
        // get a network reply
        observe_signature_click = new MutableLiveData<>();
        User user = MainActivity.getuser();
        super.onCreate(savedInstanceState);
        Selected_Event = ClickedItem;
        viewEventBinding = ActivityViewEventBinding.inflate(getLayoutInflater());
        this.eventmodel = event_List.get(Selected_Event);
        if(check_owner(user.getUserId(),a.get(Selected_Event).getOrganizer().getEmail())) {viewEventBinding.edit.setVisibility(View.VISIBLE);}
        try
        {
            serverUri = new URI("ws://192.168.43.108:8085");
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        final webSocketClient wb = new webSocketClient(serverUri);
        wb.connect();
        WebSocketClients = wb;


        viewEventBinding.startsigning.setVisibility(eventmodel.getOwner().equals(user.getUserId())?View.VISIBLE:View.INVISIBLE);


        Viewattendeesfragment fragment = new Viewattendeesfragment();
        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.attendeesfragment, fragment)
                    .commit();



        viewEventBinding.Summary.setText(event_List.get(Selected_Event).getTitle()==null?" ":event_List.get(Selected_Event).getTitle());
        viewEventBinding.location.setText(event_List.get(Selected_Event).getLocation()==null?" ":event_List.get(Selected_Event).getLocation());
        viewEventBinding.Owner.setText(event_List.get(Selected_Event).getOwner()==null?" ":event_List.get(Selected_Event).getOwner());
        viewEventBinding.Description.setText(event_List.get(Selected_Event).getDescription()==null?" ":event_List.get(Selected_Event).getDescription());

        viewEventBinding.startsigning.setOnClickListener(v->
        {
            //think about connect in this very button and add delay to obtain server return
            if(wb.isOpen())
            {
                startSign = new Message();
                startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
                startSign.setSignable(true);
                startSign.setSender(user.getUserId());
                viewEventBinding.edit.setVisibility(View.GONE);
                wb.send(startSign.toJson());
            }
            else
            {
                recreate();
                Toast.makeText(ViewEvent.this,"An error occured please try again",Toast.LENGTH_LONG).show();
            }
        });
        viewEventBinding.edit.setOnClickListener(v->
        {
            retrofitobj = Retrofit_Base_Class.getClient();

              Intent a = new Intent(ViewEvent.this,EditEvent.class);
              startActivity(a);

        });


        viewEventBinding.sign.setOnClickListener(f->{
            viewEventBinding.addparticipant.setVisibility(View.GONE);
            tableFragment = new TableFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.attendeesfragment, tableFragment); // Replace R.id.fragmentContainer with the ID of your fragment container
            transaction.commit();});

        observer_signature = s -> {
            startSign = new Message();
            startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
            startSign.setSender(user.getUserId());


            Dialog signature_dial = _Dialog.BottomSignature(ViewEvent.this);
            signature_dial.show();
            SignatureView ss = signature_dial.findViewById(R.id.signatureView);
            signature_dial.findViewById(R.id.sign_event).setOnClickListener(view -> {
                Bitmap BitSignature = ss.getSignatureBitmap();
                startSign.setSignature(resizeBitmap(BitSignature,75,75));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitSignature.compress(Bitmap.CompressFormat.JPEG, 100, stream); // Adjust the quality value as needed
                byte[] byteArray = stream.toByteArray();
                UserMeetings userInmeet = new UserMeetings();
                UserMeetingsPK UMPK = new UserMeetingsPK();
                System.out.println(Arrays.toString(byteArray));
                UMPK.setMeetingId(event_List.get(Selected_Event).getMeetingId());
                UMPK.setUserId(user.getUserId());
                userInmeet.setUserMeetingsPK(UMPK);
                userInmeet.setSignature(byteArray);
                Retrofit retrofit = Retrofit_Base_Class.getClient();
                Request_Maker request_maker = new Request_Maker();
                Dialog cdelay =  cmeet_delay.delaydialogCircular(ViewEvent.this);
                request_maker.update_usermeets(retrofit,userInmeet,cdelay,ViewEvent.this,wb,startSign,signature_dial);
                tableFragment.changeColumnImage(tableFragment.owner_index,startSign.getSignature());

            });
            signature_dial.findViewById(R.id.restartsignature).setOnClickListener(view ->
                    ss.clearSignature());
        };
        observe_signature_click.observe(this,observer_signature);

        wb._Message.observe(this,v->
        {

            JsonObject json = JsonParser.parseString(Objects.requireNonNull(wb._Message.getValue())).getAsJsonObject();
            if(json.size() != 0)
            {
                String Sender = json.get("sender").getAsString();
                if(Sender.equals(event_List.get(Selected_Event).getOwner()))
                {
                    if(json.get("Signable") != null)
                    {
                        Signable = json.get("Signable").getAsBoolean();
                    }
                }
                if(Signable)
                {
                    viewEventBinding.addparticipant.setVisibility(View.GONE);
                    tableFragment = new TableFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.attendeesfragment, tableFragment); // Replace R.id.fragmentContainer with the ID of your fragment container
                    transaction.commit();
                    Signable = false;
                }




                viewEventBinding.addparticipant.setVisibility(View.GONE);
                if(event_List.get(Selected_Event).getAttendee() != null && json.get("Signable") != null)
                {
                }
                wb.ByteMessage.observe(this,a->{
                    ByteBuffer bytes =  wb.ByteMessage.getValue();
                    String jsons = new String(Objects.requireNonNull(bytes).array(), bytes.position(), bytes.remaining());

                    // Deserialize the JSON string to your object
                    Gson gson = new Gson();
                    Message myObject = gson.fromJson(jsons, Message.class);
                    System.out.println(jsons);
                    tableFragment.changeColumnImage(tableFragment.owner_index,myObject.getSignature());
                });
            }
        });



        setContentView(viewEventBinding.getRoot());

    }

    @Override
    protected void onDestroy()
    {
        if(WebSocketClients.isOpen())
        {
            WebSocketClients.close();
        }
        observe_signature_click.removeObserver(observer_signature);
        LUM = null;
        super.onDestroy();
    }


    /**Check if the Owner possesses a write permission on the event
     *
     * @param User - The user of the present session
     * @param Owner - The owner or Organizer of the mail
     * @return - A boolean to know if user has write permission
     */
    private boolean check_owner(String User, String Owner)
    {
        return User.equals(Owner);
    }

    public static String getCleanedDisplayNames(List<EventAttendee> attendeesList) {
        StringBuilder cleanedNames = new StringBuilder();

        for (EventAttendee attendee : attendeesList) {
            String displayName = attendee.getEmail();
            cleanedNames.append(displayName).append("\n\n");
        }

        return cleanedNames.toString();
    }

    /**
     * For now it doesnt serve much as setting an Edittext invisible
     * does not close the space it occupied
     * @param edittext Edittext to be checked if filled
     * @param String string to be set to Editext
     * @param Boolean boolean to see if the edittext has content
     */
    public void Set_Invisible_on_Empty(TextView edittext, String String, Boolean Boolean)
    {
        if(Boolean)
        {
            edittext.setText(String);
        }
        {
            edittext.setVisibility(View.INVISIBLE);
        }
    }
    public Bitmap resizeBitmap(Bitmap originalBitmap, int desiredDpWidth, int desiredDpHeight) {
        int desiredPxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, desiredDpWidth, getResources().getDisplayMetrics());
        int desiredPxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, desiredDpHeight, getResources().getDisplayMetrics());

        return Bitmap.createScaledBitmap(originalBitmap, desiredPxWidth, desiredPxHeight, true);
    }
}
//remember to setVisibility of sign to Invisible

// Also Add role as part of the message