package camtrack.cmeet.activities.login;

import static camtrack.cmeet.activities.login.data.cache_user.cache_a_user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;
import java.net.URISyntaxException;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.Home;
import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.activities.create_account.Signup;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.ActivityLoginBinding;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import camtrack.cmeet.websocket.webSocketClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// remove the shared preference code in Signup if your not able to use it as suggestion
public class login extends AppCompatActivity {
    Retrofit retrofitobj;  Intent I;    Dialog delD;

    ActivityLoginBinding binding_log;     SharedPreferences sharedPreferences; User newuser; Signup signup;

    EditText[] Editform; SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
        binding_log = ActivityLoginBinding.inflate(getLayoutInflater());
        int statusBarColor = ((ColorDrawable) binding_log.getRoot().getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(statusBarColor);
        setContentView(binding_log.getRoot());

        editor = sharedPreferences.edit();
        retrofitobj = Retrofit_Base_Class.getClient();
        Editform = new EditText[]{binding_log.userid, binding_log.password};
        binding_log.userid.setAutofillHints(sharedPreferences.getString("userid","User"));
        signup = new Signup();

        binding_log.login.setOnClickListener(v->
        {
            if (signup.isAllEditTextFilled(Editform))
            {
                LOG(retrofitobj);

            }
            else
            {
                Toast.makeText(login.this, R.string.EnterFiled, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void LOG(Retrofit rbc)
    {
        delD = cmeet_delay.delaydialogCircular(this);
        delD.show();
        Request_Route RR = rbc.create(Request_Route.class);
        Call<User> CreateUserCall = RR.login(binding_log.userid.getText().toString(),binding_log.password.getText().toString());
        CreateUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response)
            {

                if(response.body()!=null)
                {
                    newuser = response.body();
                    delD.cancel();
                    Toast.makeText(login.this, "welcome "+newuser.getDisplayName(), Toast.LENGTH_LONG).show();
                    cache_a_user(editor,newuser,null);
                    I = new Intent(login.this, Home.class);
                    startActivity(I);
                }
                else
                {
                    delD.cancel();
                    Toast.makeText(login.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t)
            {
                delD.cancel();
                Toast.makeText(login.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                //Set t to differentiate when server is not reachable and false login and timeout
            }
        });

    }
}