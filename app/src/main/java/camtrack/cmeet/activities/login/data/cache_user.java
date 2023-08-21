package camtrack.cmeet.activities.login.data;

import android.content.SharedPreferences;

import camtrack.cmeet.activities.login.model.User;

public class cache_user {
    /**
     * Store a valid User in the apps  cache directory on Login, the user details are gotten from the user Model
     * @param editor sharedpreference editor
     * @param user User of session
     */
    public static void cache_a_user(SharedPreferences.Editor editor, User user)
    {
        editor.putString("userId", user.getUserId());
        editor.putString("displayName", user.getDisplayName());
        editor.putString("number", user.getNumber());
        editor.putString("department", user.getDepartment());
        editor.apply();
    }

}
