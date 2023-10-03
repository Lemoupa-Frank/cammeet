package camtrack.cmeet.retrofit;



import java.util.List;


import camtrack.cmeet.activities.Events.event_model;
import camtrack.cmeet.activities.UserMeetings.UserMeetings;
import camtrack.cmeet.activities.UserMeetings.UserMeetingsPK;
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
    Call<List<event_model>> store_meets(@Body  List<event_model> lem);

    @GET("store_meet/My_meets")
    Call<List<event_model>> get_meets(@Query("userid") String userid);

    @GET("store_meet/finduserbyMeets")
    Call<List<UserMeetings>> get_attendees(@Query("meetingsId") String meetsId);

    @GET("user/login")
    Call<User> login(@Query("userId") String userId, @Query("password") String password);

    @POST("store_meet/update_meetings")
    Call<Void> update_meetes(@Body  event_model lem);

    @POST("store_meet/update_user_meetings")
    Call<Void> user_meets(@Body UserMeetings UM);

    @GET("user/MyMeeting")
    Call<UserMeetings> MyMeeting(@Body UserMeetingsPK UPK);


}

