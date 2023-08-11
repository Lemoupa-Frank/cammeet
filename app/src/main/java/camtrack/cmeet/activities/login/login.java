package camtrack.cmeet.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import camtrack.cmeet.activities.MainActivity;
import camtrack.cmeet.databinding.ActivityLoginBinding;

public class login extends AppCompatActivity {
ActivityLoginBinding binding_log;
SharedPreferences sharedPreferences;
Intent I;
String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
        userid = sharedPreferences.getString("userId", "");
        binding_log = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding_log.getRoot());
        binding_log.password.getText();
        binding_log.userid.getText();
        binding_log.login.setOnClickListener(v->
        {
            if ((auth_user(binding_log.userid.getText().toString(),"frankmichel022@gmail.com")) && binding_log.password.getText().toString().equals("password"))
            {
                if(binding_log.userid.getText().toString().equals(userid))
                {
                    Toast.makeText(login.this, "logged user matches cached user", Toast.LENGTH_LONG).show();
                    //maintain cache user
                }
                else
                {
                    Toast.makeText(login.this, "logged user does not matches cached user", Toast.LENGTH_LONG).show();
                    //Destroy Cache user and replace with data online
                }
                I = new Intent(login.this, MainActivity.class);
                startActivity(I);
            }
            else
            {
                Toast.makeText(login.this, "please verify name", Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean auth_user(String User, String valid_user)
    {
        return User.equals(valid_user);
    }
}