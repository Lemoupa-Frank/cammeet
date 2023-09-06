package camtrack.cmeet;

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
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.activities.Events.event_model;
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
    public void getAttendees(Retrofit retrofitObject, String meetsId)
    {
        Request_Route RR = retrofitObject.create(Request_Route.class);
        Call<List<UserMeetings>> getAttendees = RR.get_attendees(meetsId);
        try
        {
            ViewEvent.LUM = getAttendees.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, UserMeetings> convertAttendeesToHashMap(List<UserMeetings> ListofUserMeetings) {
        HashMap<String, UserMeetings> UserMeetingsHashMap = new HashMap<>();

        for (UserMeetings UM : ListofUserMeetings) {
            UserMeetingsHashMap.put(UM.getUserMeetingsPK().getUserId(), UM);
        }

        return UserMeetingsHashMap;
    }
}
//http://zqktlwiuavvvqqt4ybvgvi7tyo4hjl5xgfuvpdf6otjiycgwqbym2qad.onion/wiki/The_Matrix
