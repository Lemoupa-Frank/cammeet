package camtrack.cmeet.retrofit;

import camtrack.cmeet.activities.login.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
// necessary imports to be used 

//interface that will define all request to be used 
public interface Request_Route
{
    @POST("user")
    Call<Void> create_User(@Body User user);
    @GET("user/login")
    Call<User> login(@Query("userId") String userId, @Query("password") String password);
}

