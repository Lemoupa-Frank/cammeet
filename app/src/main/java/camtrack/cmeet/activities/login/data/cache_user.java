package camtrack.cmeet.activities.login.data;

import camtrack.cmeet.activities.login.model.User;

public class cache_user {
   public static User Cuser;

    /**
     * Gets the User from registered in the model class
     * @param user
     */
    public void setLoggedInUser(User user) {
        this.Cuser = user;
    }

    /**
     * Store a valid User in the apps  cache directory on Login, the user details are gotten from the user Model
     * @param user
     * @return
     */
    public boolean cache_user(User user)
    {
        if(true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * gets the user from the apps  cached directory
     * @return
     */
    public User getCuser()
    {
        //gets user from the cached directory
        return getCuser();
    }
}
