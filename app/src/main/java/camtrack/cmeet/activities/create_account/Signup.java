package camtrack.cmeet.activities.create_account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.splashscreen.SplashScreen;

import com.google.errorprone.annotations.Var;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.cmeet_delay;
import camtrack.cmeet.activities.login.login;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.SignupBinding;
import camtrack.cmeet.retrofit.Request_Route;
import camtrack.cmeet.retrofit.Retrofit_Base_Class;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// creating account you enter all information
// if user already exist and chooses to login rather, if he logs in then the cache user is recreated from online data
// Note you can set the autofill hint
// when Launching  check if application has a stored user in shared preferences if so skipp to dashboard
public class Signup extends AppCompatActivity {
    SignupBinding signupbinding;
    Spinner Department_Spinner;
    EditText[] Editform;
    Dialog delaydialog;

    Retrofit retrofitobj;
    Request_Route request_route;
    User newuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        signupbinding = SignupBinding.inflate(getLayoutInflater());
        Department_Spinner = signupbinding.department;
        Editform = new EditText[]{signupbinding.username, signupbinding.Email, signupbinding.phone, signupbinding.password};
        set_spinner(Department_Spinner,Signup.this, R.array.Departments);
        retrofitobj = Retrofit_Base_Class.getClient();
        ConstraintLayout CL = signupbinding.getRoot();
        int statusBarColor = ((ColorDrawable) CL.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(statusBarColor);
        setContentView(signupbinding.getRoot());
        // Do assynchronous calls to database to check if user exist
        signupbinding.signup.setOnClickListener(v->
        {
            if(isAllEditTextFilled(Editform))
            {
                newuser = new User(signupbinding.Email.getText().toString(),signupbinding.username.getText().toString(),signupbinding.phone.getText().toString(),signupbinding.department.getSelectedItem().toString());
                newuser.set_password(signupbinding.password.getText().toString());
                newuser.setRole("MEMBER");
                Creat_User(request_route,retrofitobj);
            }
            else
            {
                Toast.makeText(Signup.this, "Sorry User Could not be Created", Toast.LENGTH_LONG).show();
            }
        });
        signupbinding.loginSignPage.setOnClickListener(c->
        {
            Intent i  = new Intent(Signup.this, login.class);
            startActivity(i);
        });
    }

    /**Instantiating a spinner using an array in Values/Strings
     *the spinner layout here is simple_spinner_item
     * @param spinner - The instantiated spinner View to be populated
     * @param context - The Context of the parent Activity
     * @param array_resource_value - The resource value containing the array ex R.array.ResourceName
     */
    public void set_spinner(Spinner spinner, Context context, int array_resource_value)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                array_resource_value, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public boolean isAllEditTextFilled(EditText[] editTexts)
    {
        boolean isfilled = true;
        for (EditText editText : editTexts)
        {
            if (editText.getText().length() == 0)
            {
                shakeEditText(editText);
                isfilled = false;
            }
            else if(editText.getInputType() == 33)
            {
              if(!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches())
              {
                  shakeEditText(editText);
                  isfilled = false;
              }
            }
            else if(editText.getInputType() == 3)
            {
                if(!Patterns.PHONE.matcher(editText.getText().toString()).matches())
                {
                    shakeEditText(editText);
                    isfilled = false;
                }
            }
        }
        return isfilled;
    }

    /**
     *
     * @param editText The view is transversely displaced on animation start
     */
    private void shakeEditText(EditText editText) {
        // Calculate the desired translation distance
        int shakeDistance = 50;

        // Create the animation
        Animation shakeAnimation = new TranslateAnimation(0, shakeDistance, 0, 0);
        shakeAnimation.setDuration(100);
        shakeAnimation.setRepeatCount(7);
        shakeAnimation.setRepeatMode(Animation.REVERSE);

        // Apply the animation to the EditText
        editText.startAnimation(shakeAnimation);
    }
    public void Creat_User(Request_Route RR, Retrofit rbc)
    {
        delaydialog = cmeet_delay.delaydialogCircular(this);
        delaydialog.show();
        RR = rbc.create(Request_Route.class);
        Call<Void> CreateUserCall =RR.create_User(newuser);
        CreateUserCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void>  response)
            {
                if(response.isSuccessful())
                {
                    delaydialog.cancel();
                    Toast.makeText(Signup.this, R.string.TrustMessage, Toast.LENGTH_LONG).show();
                    Intent t = new Intent(Signup.this,login.class);
                    startActivity(t);
                }
                else
                {
                    delaydialog.cancel();
                    Toast.makeText(Signup.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t)
            {
                delaydialog.cancel();
                Toast.makeText(Signup.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}