package camtrack.cmeet.retrofit;

import camtrack.cmeet.activities.login.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
// necessary imports to be used 

//interface that will define all request to be used 
public interface Request_Route {

    @POST("user")
    Call<Void> create_User(@Body User user);
}

