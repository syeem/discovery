package travnet.discovery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//View Holder for picture cards
public class CardBlogViewHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail;
    TextView title;
    TextView extract;
    Button like_button;
    TextView likes;
    TextView activity;
    TextView location;
    BarUploaderViewHolder uploader;

    public CardBlogViewHolder(View itemView) {
        super(itemView);
        uploader = new BarUploaderViewHolder ();
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        title = (TextView) itemView.findViewById(R.id.title);
        extract = (TextView) itemView.findViewById(R.id.extract);
        like_button = (Button) itemView.findViewById(R.id.like_button);
        likes = (TextView) itemView.findViewById(R.id.likes);
        activity = (TextView) itemView.findViewById(R.id.activity);
        location = (TextView) itemView.findViewById(R.id.location);
        uploader.name = (TextView) itemView.findViewById(R.id.name);
        uploader.pp = (ImageView) itemView.findViewById(R.id.pp);

    }

    public void addLikeCallback(final DataBlogCard dataBlogCard, final PictureFragment.ImageAdapter imageAdapter, final int position) {
        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBlogCard.likes++;
                dataBlogCard.isLiked = true;

                like_button.setText("LIKED");
                like_button.setOnClickListener(null);
                likes.setText(dataBlogCard.likes + " People Likes this");
            }
        });
    }


}
