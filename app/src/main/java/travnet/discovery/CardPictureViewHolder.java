package travnet.discovery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangfeizc.avatarview.AvatarView;

//View Holder for picture cards
public class CardPictureViewHolder extends RecyclerView.ViewHolder {
    TextView description;
    ImageView image;
    ImageButton like_button;
    ImageButton add_to_bl_button;
    TextView likes;
    TextView activity;
    TextView location;
    AvatarView uploaderPic;
    BarUploaderViewHolder uploader;

    public CardPictureViewHolder(View itemView) {
        super(itemView);
        uploader = new BarUploaderViewHolder ();
        description = (TextView) itemView.findViewById(R.id.description);
        image = (ImageView) itemView.findViewById(R.id.image);
        like_button = (ImageButton) itemView.findViewById(R.id.like_button);
        add_to_bl_button = (ImageButton) itemView.findViewById(R.id.add_to_bl_button);
        likes = (TextView) itemView.findViewById(R.id.likes);
        activity = (TextView) itemView.findViewById(R.id.activity);
        location = (TextView) itemView.findViewById(R.id.location);
        //uploader.name = (TextView) itemView.findViewById(R.id.name);
        //uploader.pp = (ImageView) itemView.findViewById(R.id.pp);
        uploaderPic = (AvatarView) itemView.findViewById(R.id.pp);
    }



    public void addLikeCallback(final DataPictureCard dataPictureCard, final HomeFragment.ImageAdapter imageAdapter, final int position) {
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPictureCard.likes++;
                dataPictureCard.isLiked = true;

                //like_button.setVisibility(View.GONE);
                like_button.setImageResource(R.drawable.ic_liked);
                add_to_bl_button.setVisibility(View.VISIBLE);
                location.setVisibility(View.VISIBLE);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f );
                location.startAnimation(fadeIn);
                fadeIn.setDuration(1200);
                fadeIn.setFillAfter(true);
                //likes.setText(dataPictureCard.likes + " People Likes this");
            }
        });
    }


}
