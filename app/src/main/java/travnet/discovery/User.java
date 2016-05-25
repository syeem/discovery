package travnet.discovery;

import android.graphics.Bitmap;

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
    private Bitmap profilePic;

    private List<String> interests;

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }


    public void updateUser (String id, String name, String email, String location, String hometown, Bitmap profilePic) {
        this.name = name;
        this.email = email;
        this.hometown = hometown;
        this.location = location;
        this.profilePic = profilePic;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }


    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
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
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
