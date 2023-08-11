package camtrack.cmeet.activities.Events;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import camtrack.cmeet.databinding.ActivityEditActivtyBinding;


public class Edit_Activity extends AppCompatActivity
{
    ActivityEditActivtyBinding editActivtyBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        editActivtyBinding = ActivityEditActivtyBinding.inflate(getLayoutInflater());
        setContentView(editActivtyBinding.getRoot());
    }
}