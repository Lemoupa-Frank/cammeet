package camtrack.cmeet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import camtrack.cmeet.R;
import camtrack.cmeet.activities.Events.EventFragment;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_main);
        if (savedInstanceState == null)
        {
            MainActivity fragment = new MainActivity();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}