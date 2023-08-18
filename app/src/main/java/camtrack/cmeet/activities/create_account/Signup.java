package camtrack.cmeet.activities.create_account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import camtrack.cmeet.R;
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

    Retrofit retrofitobj;
    Request_Route request_route;

    User newuser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        signupbinding = SignupBinding.inflate(getLayoutInflater());
        Department_Spinner = signupbinding.department;
        Editform = new EditText[]{signupbinding.username, signupbinding.Email, signupbinding.phone, signupbinding.password};
        set_spinner(Department_Spinner,Signup.this, R.array.Departments);
        retrofitobj = Retrofit_Base_Class.getClient();
        setContentView(signupbinding.getRoot());
        // Do assynchronous calls to database to check if user exist
        signupbinding.signup.setOnClickListener(v->
        {
            if(isAllEditTextFilled(Editform))
            {
                NewUser(signupbinding.Email.getText().toString()
                        , signupbinding.username.getText().toString()
                        , signupbinding.phone.getText().toString()
                        , signupbinding.department.getSelectedItem().toString(),signupbinding.password.getText().toString());
                editor.putString("userId", newuser.getUserId());
                editor.putString("displayName", newuser.getDisplayName());
                editor.putString("number", newuser.getNumber());
                editor.putString("department", newuser.getDepartment());
                editor.apply();
                Creat_User(request_route,retrofitobj);
            }
            else
            {

            }
        });
        signupbinding.loginSignPage.setOnClickListener(c->
        {
            Intent i  = new Intent(Signup.this, login.class);
            startActivity(i);
        });
    }

    /**
     * Uses the Model class found in Login to create a new user
     * @param userId - The unique identifier of the user which is the email
     * @param displayName - The name of the user too creat account
     * @param number - The phone number of the user
     * @param department- The Department of the user
     */
    public void NewUser(String userId, String displayName, String number,String department, String password)
    {
        this.newuser = new User(userId,displayName,number,department, password);
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
    private boolean isAllEditTextFilled(EditText[] editTexts)
    {
        boolean isfilled = true;
        String A = getString(R.string.EnterFiled);
        for (EditText editText : editTexts)
        {
            if (editText.getText().length() == 0)
            {
                Toast.makeText(Signup.this, A +" "+ editText.getHint(), Toast.LENGTH_SHORT).show();
                shakeEditText(editText);
                isfilled = false;
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
        RR = rbc.create(Request_Route.class);
        Call<Void> CreateUserCall =RR.create_User(newuser);
        CreateUserCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void>  response)
            {
                if(response.isSuccessful())
                {
                    Toast.makeText(Signup.this, newuser.toString(), Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(Signup.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t)
            {
                Toast.makeText(Signup.this, "On failure Exception " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}