package camtrack.cmeet;

import static camtrack.cmeet.activities.Events.ViewEvent.MutableLUM;
import static camtrack.cmeet.activities.MainActivity.user;

import android.app.Dialog;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import camtrack.cmeet.activities.Events.ViewEvent;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.UserMeetings.UserMeetingsPK;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.activities.Events.event_model;
import camtrack.cmeet.websocket.Message;
import camtrack.cmeet.websocket.webSocketClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A general class to handle and make calls
 * using Retrofit also has a some utility functions
 * like a UserMeetings converter to Hashmaps
 */

public class Request_Maker
{
    public void store_todays_meets(Retrofit retrofitObject, Context con, List<event_model> a)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<Void> CreateUserCall = RR.store_meets(a);
        CreateUserCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response)
            {
                if(response.isSuccessful())
                {
                    Toast.makeText(con, R.string.TrustMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(con, response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t)
            {
                Toast.makeText(con, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Retrofit call to get the list of users and their roles in selected meeting
     * Onsucess modifies LUM the static variable that stores the list
     * @param retrofitObject Retrofit object already instantiated
     * @param meetsId The id of the meeting you wish to get the Users
     */
    public void getAttendees(Retrofit retrofitObject, String meetsId, Context con)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<List<UserMeetings>> _getAttendees = RR.get_attendees(meetsId);
        _getAttendees.enqueue(new Callback<List<UserMeetings>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserMeetings>> call, @NonNull Response<List<UserMeetings>> response) {
                if(response.isSuccessful())
                {
                    ViewEvent.LUM = response.body();
                    ViewEvent.MutableLUM.postValue(ViewEvent.LUM);
                    Toast.makeText(con,"Success",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(con,"Something Went wrong",Toast.LENGTH_LONG).show();
                    System.out.println("********************************************************");
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserMeetings>> call, @NonNull Throwable t) {
                {
                    Toast.makeText(con,t.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void meetings(Retrofit retrofitObject, event_model cm, Dialog delaydialog, Context con)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<Void> Update_Meetings_Call = RR.meets(cm);
        delaydialog.show();
        Update_Meetings_Call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                delaydialog.cancel();
                if(response.isSuccessful())
                {
                    Toast.makeText(con,"Success",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(con,"Something Went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                delaydialog.cancel();
                Toast.makeText(con,"Check Your Connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void  update_usermeets(Retrofit retrofitObject, UserMeetings UM, Dialog delaydialog, Context con, webSocketClient web_Socket, Message startSign, Dialog signature_dial)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<Void> Update_Meetings_Call = RR.user_meets(UM);
        delaydialog.show();
        Update_Meetings_Call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                delaydialog.cancel();
                if(response.isSuccessful())
                {
                    if(web_Socket.isOpen())
                    {
                        web_Socket.send(startSign.toJson());
                    }
                    signature_dial.cancel();
                }
                else
                {
                    Toast.makeText(con,"Something Went wrong",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                delaydialog.cancel();
                signature_dial.cancel();
                Toast.makeText(con,"Check Your Connection",Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Converts list of Attendees into a haspMap to allow better manipulation
     * of attendees including checking if there exist
     * @param ListofUserMeetings The list of Attendees for a particular Meeting
     * @return Returns a hashmap of Attendees
     */
    public HashMap<String, UserMeetings> convertAttendeesToHashMap(List<UserMeetings> ListofUserMeetings) {
        HashMap<String, UserMeetings> UserMeetingsHashMap = new HashMap<>();

        for (UserMeetings UM : ListofUserMeetings) {
            UserMeetingsHashMap.put(UM.getUserMeetingsPK().getUserId(), UM);
        }

        return UserMeetingsHashMap;
    }



}
//http://zqktlwiuavvvqqt4ybvgvi7tyo4hjl5xgfuvpdf6otjiycgwqbym2qad.onion/wiki/The_Matrix
/*
            new Thread(()->
            {
                try {
                    getAttendees.execute();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }).start();
 */