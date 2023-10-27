package camtrack.cmeet.activities.login.model;

public class User {
    private final String userId;
    private final String display_name;
    private final String number;
    private final String department;
    private String role;
    private  String password;

    /**A constructor to fully initialize user for
     * his session
     * @param userId - user e-mail
     * @param display_name - user disply name
     * @param number  - user phone number
     * @param department - user department
     */
    public User(String userId, String display_name, String number, String department) {
        this.userId = userId;
        this.display_name = display_name;
        this.number = number;
        this.department = department;
    }

    /**A constructor used for user which are not logged
     * this helpfull in adding attendees or storing attendess
     */
    public User()
    {
        this.userId = null;
        this.display_name = null;
        this.number = null;
        this.department = null;
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

    public void set_password(String password){this.password = password;}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
