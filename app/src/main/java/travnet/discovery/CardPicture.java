package travnet.discovery;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CardPicture {

    //Data Structure for picture cards
    public static class DataPictureCard {
        String description;
        String link;
        String likes;
        String location;
        String activity;
        DataUploaderBar dataUploaderBar;

        public DataPictureCard() {

        }

        public DataPictureCard(String description, String link, String likes, String location, String activity, String uploader_name, String uploader_pp) {
            dataUploaderBar = new DataUploaderBar();
            this.description = description;
            this.link = link;
            this.likes = likes;
            this.location = location;
            this.activity = activity;
            this.dataUploaderBar.uploader_name = uploader_name;
            this.dataUploaderBar.uploader_pp = uploader_pp;
        }

    }

    //View Holder for picture cards
    public static class CardPictureViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView image;
        Button like_button;
        Button add_to_bl_button;
        TextView likes;
        TextView activity;
        TextView location;
        BarUploaderViewHolder uploader;

        public CardPictureViewHolder(View itemView) {
            super(itemView);
            uploader = new BarUploaderViewHolder ();
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.image);
            like_button = (Button) itemView.findViewById(R.id.like_button);
            add_to_bl_button = (Button) itemView.findViewById(R.id.add_to_bl_button);
            likes = (TextView) itemView.findViewById(R.id.likes);
            activity = (TextView) itemView.findViewById(R.id.activity);
            location = (TextView) itemView.findViewById(R.id.location);
            uploader.name = (TextView) itemView.findViewById(R.id.name);
            uploader.pp = (ImageView) itemView.findViewById(R.id.pp);
            addCallbacks();
        }

        private void addCallbacks() {
            like_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //like_button.setText("Liked");
                    like_button.setVisibility(View.GONE);
                    add_to_bl_button.setVisibility(View.VISIBLE);
                    location.setVisibility(View.VISIBLE);
                    AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
                    location.startAnimation(fadeIn);
                    fadeIn.setDuration(1200);
                    fadeIn.setFillAfter(true);
                }
            });
        }


    }




}


