package camtrack.cmeet;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.activities.Events.event_model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Request_Maker
{
    public void store_todays_meets(Retrofit rbc, Context con, List<event_model> a)
    {
        Request_Route RR = rbc.create(Request_Route.class);
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
}
