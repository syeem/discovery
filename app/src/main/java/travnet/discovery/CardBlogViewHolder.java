package travnet.discovery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangfeizc.avatarview.AvatarView;

//View Holder for picture cards
public class CardBlogViewHolder extends RecyclerView.ViewHolder {
    View cardView;

    ImageView thumbnail;
    TextView title;
    TextView extract;
    ImageButton like_button;
    TextView likes;
    TextView activity;
    TextView location;
    AvatarView uploaderPic;
    BarUploaderViewHolder uploader;

    public CardBlogViewHolder(View itemView) {
        super(itemView);
        cardView = itemView;
        uploader = new BarUploaderViewHolder ();
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        title = (TextView) itemView.findViewById(R.id.title);
        extract = (TextView) itemView.findViewById(R.id.extract);
        like_button = (ImageButton) itemView.findViewById(R.id.like_button);
        likes = (TextView) itemView.findViewById(R.id.likes);
        activity = (TextView) itemView.findViewById(R.id.activity);
        location = (TextView) itemView.findViewById(R.id.location);
        //uploader.name = (TextView) itemView.findViewById(R.id.name);
        //uploader.pp = (ImageView) itemView.findViewById(R.id.pp);
        uploaderPic = (AvatarView) itemView.findViewById(R.id.pp);

    }

    public void addLikeCallback(final DataBlogCard dataBlogCard, final HomeFragment.ImageAdapter imageAdapter, final int position) {
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBlogCard.likes++;
                dataBlogCard.isLiked = true;

                like_button.setOnClickListener(null);
                //likes.setText(dataBlogCard.likes + " People Likes this");
            }
        });
    }


}
