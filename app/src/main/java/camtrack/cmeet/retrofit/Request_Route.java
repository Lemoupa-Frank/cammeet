package camtrack.cmeet.retrofit;



import java.util.List;


import camtrack.cmeet.activities.Events.event_model;
import camtrack.cmeet.activities.login.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
// necessary imports to be used

//interface that will define all request to be used
public interface Request_Route
{
    @POST("user")
    Call<Void> create_User(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("store_meet")
    Call<Void> store_meets(@Body  List<event_model> lem);

    @GET("store_meet/My_meets")
    Call<List<event_model>> get_meets(@Query("userid") String userid);


    @GET("user/login")
    Call<User> login(@Query("userId") String userId, @Query("password") String password);
}

