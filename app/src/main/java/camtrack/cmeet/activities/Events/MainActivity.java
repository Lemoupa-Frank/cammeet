package camtrack.cmeet.activities.Events;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import camtrack.cmeet.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_main);
        if (savedInstanceState == null)
        {
            EventFragment fragment = new EventFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }

    }
}