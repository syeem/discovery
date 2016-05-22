package travnet.discovery;

/**
 * Created by Sunny on 21/5/2016.
 */
public class User {
    private static User ourInstance = new User();
    private String userID;

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }


}
