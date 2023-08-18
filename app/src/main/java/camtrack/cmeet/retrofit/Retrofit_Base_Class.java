package camtrack.cmeet.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//necessary imports

public class Retrofit_Base_Class {

//base url 
    public static String BASE_URL ="http://192.168.43.108:8080/";

// declaring retrofit class
    public static Retrofit retrofit;
    
// creating a converter and make it lenient    
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();
// method to instantiate and return a retrofit object            
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
