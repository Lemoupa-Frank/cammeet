package camtrack.cmeet.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

public class cmeet_alert {

    public static void displayAlertDialog(Context context, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder
                .setMessage(Message)
                .create();
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
        }, 3000);
    }
}
