package camtrack.cmeet.activities;

import static camtrack.cmeet.activities.login.data.cache_user.cache_a_user;

import camtrack.cmeet.activities.login.model.User;
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
    User user;
    public static String Role;
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
        Role = user.getRole();
        fragment = new MainActivity();
        roleFragment = new RoleFragment();
        matricesFragment = new matrices_fragment();
        setContentView(R.layout.frame_main);
        if (savedInstanceState == null) {
            matricesFragment.setDepart_name(user.getDepartment());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, roleFragment)
                    .add(R.id.container, fragment)
                    .add(R.id.container, matricesFragment)
                    .hide(roleFragment)
                    .hide(matricesFragment)
                    .commit();
        }
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(v ->
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(roleFragment)
                        .hide(matricesFragment)
                        .show(fragment)
                        .commit());

        ImageView matrix = findViewById(R.id.matrix);

        matrix.setOnClickListener(v ->
        {
            if (Role.equals("CEO")) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment)
                        .hide(roleFragment)
                        .show(matricesFragment)
                        .commit();
            } else if (Role.equals("Human Resource Head")) {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment)
                        .hide(roleFragment)
                        .show(matricesFragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .hide(fragment)
                        .hide(matricesFragment)
                        .show(roleFragment)
                        .commit();
            }
            // (Role.equals("MEMBER"))
        });
    }
    /*

     */
}