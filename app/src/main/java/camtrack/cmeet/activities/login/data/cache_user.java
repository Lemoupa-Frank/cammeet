package camtrack.cmeet.activities.login.data;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import camtrack.cmeet.activities.login.model.User;

public class cache_user {
    /**
     * Store a valid User in the apps  cache directory, or retrieves a stored user in cache
     * @param editor sharedpreference editor
     * @param user User of session which will have its attributes stored in in the shared preference
     * @param  sh if you wish to store a user in cache then add a non null instance of this parameter
     */
    public static User cache_a_user(@Nullable SharedPreferences.Editor editor, @Nullable User user, @Nullable SharedPreferences sh)
    {
        if (editor != null && user != null )
        {
            editor.putString("userId", user.getUserId());
            editor.putString("displayName", user.getDisplayName());
            editor.putString("number", user.getNumber());
            editor.putString("department", user.getDepartment());
            editor.apply();
            return user;
        }
        else
        {
            assert sh != null;
            user = new User(sh.getString("userId",".."),
                    sh.getString("displayName","..."),
                    sh.getString("number","..."),
                    sh.getString("department","...")
            );
            return user;
        }
    }
// check how you can create a user from cache
}
