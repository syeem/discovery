package travnet.discovery;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class BucketListActivity extends AppCompatActivity {

    List<DataBucketListCard> userBucketList;
    BucketListItemAdapter bucketListItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userBucketList = new ArrayList<DataBucketListCard>();
        bucketListItemAdapter = new BucketListItemAdapter(this);

        //Stub
        String link = "http://www.planwallpaper.com/static/images/4-Nature-Wallpapers-2014-1_ukaavUI.jpg";
        DataBucketListCard data1 = new DataBucketListCard(link , "Bali, Indonesia", "Activity");
        DataBucketListCard data2 = new DataBucketListCard(link, "Lombok, Indonesia", "Activity");
        DataBucketListCard data3 = new DataBucketListCard(link, "Krabi, Thailand", "Activity");
        userBucketList.add(data1);
        userBucketList.add(data2);
        userBucketList.add(data3);


        RecyclerView userBucketList = (RecyclerView) findViewById(R.id.user_bucket_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userBucketList.setLayoutManager(linearLayoutManager);
        userBucketList.setAdapter(bucketListItemAdapter);



    }

    private class BucketListItemAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

        private LayoutInflater inflater;
        private DisplayImageOptions options;


        BucketListItemAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_loading)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .build();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return userBucketList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_bucket_list_item, parent, false);
            CardBucketListItemViewHolder cardBucketListItemViewHolder = new CardBucketListItemViewHolder(view);
            return cardBucketListItemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            CardBucketListItemViewHolder cardBucketListItemViewHolder = (CardBucketListItemViewHolder) holder;
            cardBucketListItemViewHolder.location.setText(userBucketList.get(position).location);
            ImageLoader.getInstance().displayImage(userBucketList.get(position).imageLink, cardBucketListItemViewHolder.image, options, null);


        }
    }


    private class CardBucketListItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView location;

        public CardBucketListItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            location = (TextView) itemView.findViewById(R.id.location);

        }
    }

}
