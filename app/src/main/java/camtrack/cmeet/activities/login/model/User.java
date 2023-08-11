package camtrack.cmeet.activities.login.model;

public class User {
    private final String userId;
    private final String displayName;
    private final String number;
    private final String department;

    /**A constructor to fully initialize user for
     * his session
     * @param userId - user e-mail
     * @param displayName - user disply name
     * @param number  - user phone number
     * @param department - user department
     */
    public User(String userId, String displayName, String number, String department) {
        this.userId = userId;
        this.displayName = displayName;
        this.number = number;
        this.department = department;
    }

    /**A constructor used for user which are not logged
     * this helpfull in adding attendees or storing attendess
     * @param userId user email
     * @param displayName users display name
     */
    public User(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
        department = null;
        number = null;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getNumber() {
        return number;
    }

    public String getDepartment() {
        return department;
    }
}
