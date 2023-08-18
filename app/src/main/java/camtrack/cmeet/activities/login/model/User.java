package camtrack.cmeet.activities.login.model;

public class User {
    private final String userId;
    private final String display_name;
    private final String number;
    private final String department;
    private final String password;

    /**A constructor to fully initialize user for
     * his session
     * @param userId - user e-mail
     * @param display_name - user disply name
     * @param number  - user phone number
     * @param department - user department
     * @param password -users database password
     */
    public User(String userId, String display_name, String number, String department, String password) {
        this.userId = userId;
        this.display_name = display_name;
        this.number = number;
        this.department = department;
        this.password = password;
    }

    /**A constructor used for user which are not logged
     * this helpfull in adding attendees or storing attendess
     * @param userId user email
     * @param display_name users display name
     */
    public User(String userId, String display_name) {
        this.userId = userId;
        this.display_name = display_name;
        department = null;
        number = null;
        password = null;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getNumber() {
        return number;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }
}
