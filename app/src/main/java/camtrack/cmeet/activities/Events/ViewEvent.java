package camtrack.cmeet.activities.Events;




import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.ByteArrayOutputStream;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
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


    static public MutableLiveData<String> observe_signature_click;

    static Observer<String> observer_signature;
    public List<UserMeetings> LUM = new ArrayList<>();
    public List<event_model> event_List = new ArrayList<>();
    int Selected_Event;
    private webSocketClient WebSocketClients;
    boolean Signable = false;
    Message startSign;
    event_model eventmodel;
    URI serverUri = null;
    TableFragment tableFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        viewEventBinding = ActivityViewEventBinding.inflate(getLayoutInflater());
        ConstraintLayout backgroundLayout = viewEventBinding.getRoot();
        int statusBarColor = ((ColorDrawable) backgroundLayout.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(statusBarColor);
        observe_signature_click = new MutableLiveData<>();
        User user = MainActivity.getuser();
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        java.io.Serializable serializable_event_list_Extra = intent.getSerializableExtra("EVENT_LIST");
        java.io.Serializable serializable_User_List_Extra = intent.getSerializableExtra("UserMeetingList");
        List<?> serializable_Event_List = (List<?>) serializable_event_list_Extra;
        if (serializable_Event_List != null) {
            for (Object item : serializable_Event_List) {
                if (item instanceof event_model) {
                    event_model event = (event_model) item;
                    event_List.add(event);
                }
            }
        }
        List<?> serializableUserList = (List<?>) serializable_User_List_Extra;
        if (serializableUserList != null) {
            for (Object item : serializableUserList) {
                if (item instanceof UserMeetings) {
                    UserMeetings userMeetings = (UserMeetings) item;
                    LUM.add(userMeetings);
                }
            }
        }

        Selected_Event = intent.getIntExtra("selected_item",0);
        this.eventmodel = event_List.get(Selected_Event);
        if(check_owner(user.getUserId(),event_List.get(Selected_Event).getOwner())) {viewEventBinding.edit.setVisibility(View.VISIBLE);}
        try
        {
            serverUri = new URI("ws://192.168.43.107:8085");
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        final webSocketClient wb = new webSocketClient(serverUri);
        wb.ClickedItem = Selected_Event;
        wb.event_modelList= event_List;
        wb.connect();
        WebSocketClients = wb;


        viewEventBinding.startsigning.setVisibility(eventmodel.getOwner().equals(user.getUserId())?View.VISIBLE:View.INVISIBLE);


        Viewattendeesfragment fragment = new Viewattendeesfragment();
        fragment.cmeet_list = event_List;
        fragment.ClickedItem = Selected_Event;
        fragment.Viewattendees_List_of_User_Meetings = LUM;
        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.attendeesfragment, fragment)
                    .commit();



        viewEventBinding.Summary.setText(event_List.get(Selected_Event).getTitle()==null?" ":event_List.get(Selected_Event).getTitle());
        viewEventBinding.location.setText(event_List.get(Selected_Event).getLocation()==null?" ":event_List.get(Selected_Event).getLocation());
        viewEventBinding.Owner.setText(event_List.get(Selected_Event).getOwner()==null?" ":event_List.get(Selected_Event).getOwner());
        viewEventBinding.Description.setText(event_List.get(Selected_Event).getDescription()==null?" ":event_List.get(Selected_Event).getDescription());
        viewEventBinding.meetingid.setText(event_List.get(Selected_Event).getMeetingId());

        viewEventBinding.startsigning.setOnClickListener(v->
        {
            if(wb.isOpen())
            {
                startSign = new Message();
                startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
                startSign.setSignable(!Signable);
                startSign.setSender(user.getUserId());
                viewEventBinding.edit.setVisibility(View.GONE);
                wb.send(startSign.toJson());
            }
            else
            {
                if(!Signable)
                {
                    recreate();
                    Toast.makeText(ViewEvent.this,"An error occured please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
        viewEventBinding.edit.setOnClickListener(v->
        {
            Intent a = new Intent(ViewEvent.this,EditEvent.class);
            a.putExtra("EVENT_LIST", new ArrayList<>(event_List));
            a.putExtra("selected_item", Selected_Event);
            a.putExtra("UserMeetingList", new ArrayList<>(LUM));
              startActivity(a);

        });


        // observes for click on signature table view
        observer_signature = s ->
        {
            startSign = new Message();
            startSign.setMeetingId(event_List.get(Selected_Event).getMeetingId());
            startSign.setSender(user.getUserId());
            Dialog signature_dial = _Dialog.BottomSignature(ViewEvent.this);
            if (Signable)
            {
                signature_dial.show();
                SignatureView ss = signature_dial.findViewById(R.id.signatureView);
                signature_dial.findViewById(R.id.sign_event).setOnClickListener(view ->
                {
                    Bitmap BitSignature = ss.getSignatureBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    BitSignature.compress(Bitmap.CompressFormat.JPEG, 50, stream); // Adjust the quality value as needed
                    byte[] byteArray = stream.toByteArray();
                    UserMeetings userInmeet = new UserMeetings();
                    UserMeetingsPK UMPK = new UserMeetingsPK();
                    System.out.println(Arrays.toString(byteArray));
                    UMPK.setMeetingId(event_List.get(Selected_Event).getMeetingId());
                    UMPK.setUserId(user.getUserId());
                    userInmeet.setUserMeetingsPK(UMPK);
                    userInmeet.setSignature_data(byteArray);
                    startSign.setSignature(byteArray);
                    userInmeet.setSignature(":cmeetSignatures" + UMPK.getUserId());
                    Retrofit retrofit = Retrofit_Base_Class.getClient();
                    Request_Maker request_maker = new Request_Maker();
                    Dialog cdelay =  cmeet_delay.delaydialogCircular(ViewEvent.this);
                    request_maker.update_usermeets(retrofit,userInmeet,cdelay,ViewEvent.this,wb,startSign,signature_dial);
                });
                signature_dial.findViewById(R.id.restartsignature).setOnClickListener(view ->
                        ss.clearSignature());
        }};
        observe_signature_click.observe(this,observer_signature);

        //observer for string message from website
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
                        if(Signable)
                        {
                            viewEventBinding.addparticipant.setVisibility(View.GONE);
                            tableFragment = new TableFragment();
                            tableFragment.Clicked_Item = Selected_Event;
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.attendeesfragment, tableFragment);
                            transaction.commit();
                            viewEventBinding.startsigning.setText(R.string.end_signing);
                        }
                        else
                        {
                            if(tableFragment == null)
                            {
                                viewEventBinding.addparticipant.setVisibility(View.GONE);
                                tableFragment = new TableFragment();
                                tableFragment.Clicked_Item = Selected_Event;
                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.attendeesfragment, tableFragment);
                                transaction.commit();
                            }
                                viewEventBinding.startsigning.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        // observer from byte message from websoket
        wb.ByteMessage.observe(this,a->{
            ByteBuffer bytes =  wb.ByteMessage.getValue();
            String jsons = new String(Objects.requireNonNull(bytes).array(), bytes.position(), bytes.remaining());

            // Deserialize the JSON string to your object
            Gson gson = new Gson();
            Message myObject = gson.fromJson(jsons, Message.class);
            System.out.println(jsons);
            int pos = -8;
            // Getting the position of the sender in the signature table, by
            // getting his position in the array of attendees
            for (int i = 0; i < event_List.get(Selected_Event).getAttendee().length; i++)
            {
                if(myObject.getSender().equals(event_List.get(Selected_Event).getAttendee()[i]))
                {
                    pos  = i;
                }
            }
            tableFragment.changeColumnImage(pos,myObject.getSignature());
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

}
//remember to setVisibility of sign to Invisible

// Also Add role as part of the message