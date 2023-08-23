package camtrack.cmeet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

public class cmeet_alert {

    public static void displayAlertDialog(Context context, String userName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Welcome, " + userName + "!");
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.TOP);
        alertDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        }, 6000);
    }
}
