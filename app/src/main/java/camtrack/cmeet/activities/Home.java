package camtrack.cmeet.activities;

import static camtrack.cmeet.activities.MainActivity.user;
import static camtrack.cmeet.activities.login.data.cache_user.cache_a_user;

import camtrack.cmeet.databinding.FrameMainBinding;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import camtrack.cmeet.R;
import camtrack.cmeet.RoleFragment;
import camtrack.cmeet.matrices_fragment;

public class Home extends AppCompatActivity {
RoleFragment roleFragment;
matrices_fragment matricesFragment;
MainActivity fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameMainBinding binding = FrameMainBinding.inflate(getLayoutInflater());
        FrameLayout backgroundLayout = binding.getRoot();
        int statusBarColor = ((ColorDrawable) backgroundLayout.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusBarColor);
        window.setNavigationBarColor(statusBarColor);
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        user = cache_a_user(null, user, sharedPreferences);

        matricesFragment = new matrices_fragment();
        fragment = new MainActivity();
        if(user.getRole().equals("MEMBER") || user.getRole().equals(""))
        {
            roleFragment = new RoleFragment();
        }
        setContentView(R.layout.frame_main);
        if (savedInstanceState == null)
        {
            matricesFragment.setDepart_name(user.getDepartment());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,roleFragment == null? matricesFragment:roleFragment)
                    .add(R.id.container, fragment)
                    .hide(roleFragment == null? matricesFragment:roleFragment)
                    .commit();
        }
        ImageView home =  findViewById(R.id.home);
        home.setOnClickListener(v ->
        {

                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(roleFragment == null? matricesFragment:roleFragment)
                        .show(fragment)
                        .commit();

        });

        ImageView matrix =  findViewById(R.id.matrix);

        matrix.setOnClickListener(v ->
        {
            String Role =  user.getRole();
            if(Role.equals("MEMBER"))
            {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment)
                        .show(roleFragment == null? matricesFragment:roleFragment)
                        .commit();
            } else if (Role.equals("CEO")) {

            }
            else
            {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment)
                        .show(roleFragment == null? matricesFragment:roleFragment)
                        .commit();
            }
        });
    }
}