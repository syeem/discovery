package travnet.discovery;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



//Data Structure for picture cards
public class DataPictureCard {
    String description;
    String link;
    int likes;
    String location;
    String activity;
    DataUploaderBar dataUploaderBar;

    boolean isLiked;

    public DataPictureCard() {
    }

    public DataPictureCard(String description, String link, int likes, String location, String activity, String uploader_name, String uploader_pp) {
        dataUploaderBar = new DataUploaderBar();
        this.description = description;
        this.link = link;
        this.likes = likes;
        this.location = location;
        this.activity = activity;
        this.dataUploaderBar.uploader_name = uploader_name;
        this.dataUploaderBar.uploader_pp = uploader_pp;

        this.isLiked = false;
    }

}








