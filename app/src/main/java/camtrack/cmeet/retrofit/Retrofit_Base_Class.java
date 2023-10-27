package camtrack.cmeet.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Retrofit_Base_Class {

//base url 
    public static String BASE_URL ="http://192.168.43.107:8080/";

// declaring retrofit class
    public static Retrofit retrofit;

    public static Retrofit retrofit_String;
    
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
    public static Retrofit getClient_String()
    {
        retrofit_String = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit_String;
    }
}
