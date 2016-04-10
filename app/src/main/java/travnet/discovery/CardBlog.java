package travnet.discovery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CardBlog {

    //Data Structure for blog cards
    public static class DataBlogCard {
        String thumbnail_url;
        String title;
        String extract;
        String likes;
        String location;
        DataUploaderBar dataUploaderBar;

        public DataBlogCard() {

        }

        public DataBlogCard(String thumbnail_url, String title, String extract, String likes, String location, String uploader_name, String uploader_pp) {
            dataUploaderBar = new DataUploaderBar();
            this.thumbnail_url = thumbnail_url;
            this.title = title;
            this.extract = extract;
            this.likes = likes;
            this.location = location;
            this.dataUploaderBar.uploader_name = uploader_name;
            this.dataUploaderBar.uploader_pp = uploader_pp;
        }

    }

    //View Holder for picture cards
    public static class CardBlogViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView extract;
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
            likes = (TextView) itemView.findViewById(R.id.likes);
            activity = (TextView) itemView.findViewById(R.id.activity);
            location = (TextView) itemView.findViewById(R.id.location);
            uploader.name = (TextView) itemView.findViewById(R.id.name);
            uploader.pp = (ImageView) itemView.findViewById(R.id.pp);

        }
    }


}

