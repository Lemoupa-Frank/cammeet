package camtrack.cmeet.activities.create_account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.login.login;
import camtrack.cmeet.activities.login.model.User;
import camtrack.cmeet.databinding.SignupBinding;
// creating account you enter all information
// if user already exist and chooses to login rather, if he logs in then the cache user is recreated from online data
// Note you can set the autofill hint
// when Launching  check if application has a stored user in shared preferences if so skipp to dashboard
public class Signup extends AppCompatActivity {
    SignupBinding signupbinding;
    Spinner Department_Spinner;
    User newuser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        signupbinding = SignupBinding.inflate(getLayoutInflater());
        Department_Spinner = signupbinding.department;
        set_spinner(Department_Spinner,Signup.this, R.array.Departments);
        setContentView(signupbinding.getRoot());
        // Do assynchronous calls to database to check if user exist
        signupbinding.signup.setOnClickListener(v->
        {
            NewUser(signupbinding.Email.getText().toString()
                    ,signupbinding.username.getText().toString()
                    ,signupbinding.phone.getText().toString()
                    ,signupbinding.department.getSelectedItem().toString());
            editor.putString("userId", newuser.getUserId());
            editor.putString("displayName", newuser.getDisplayName());
            editor.putString("number", newuser.getNumber());
            editor.putString("department", newuser.getDepartment());
            editor.apply();
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
    public void NewUser(String userId, String displayName, String number,String department)
    {
        this.newuser = new User(userId,displayName,number,department);
    }

    /**Instantiating a spinner using an array in Values/Strings
     *the spinner layout here is simple_spinner_item
     * @param spinner - The instantiated spinner to be populated
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
}