package travnet.discovery;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 21/5/2016.
 */
public class User {
    private static User ourInstance = new User();

    private String userID;
    private String name;
    private String email;
    private String hometown;
    private String location;
    private String profilePicURL;

    private int userState;

    private List<String> interests = new ArrayList<String>();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
        userState = 0;
    }


    public void updateUser (String name, String email, String location, String hometown, String profilePicURL) {
        this.name = name;
        this.email = email;
        this.hometown = hometown;
        this.location = location;
        this.profilePicURL = profilePicURL;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInterests() {
        List<String> temp = new ArrayList<String>(this.interests);
        return temp;
    }

    public void setInterests(List<String> interests) {
        this.interests.clear();
        this.interests.addAll(interests);
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
