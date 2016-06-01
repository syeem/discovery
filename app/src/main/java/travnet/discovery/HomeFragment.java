package travnet.discovery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class HomeFragment extends Fragment {
    public static final int LOADING_THRESHOLD = 2;
    public static final int NO_OF_CARDS = 5;
    public static final String URL_GET_STRING = "http://54.86.18.174/api/getCards";

    private static final int TYPE_PICTURE = 0;
    private static final int TYPE_BLOG = 1;

    public static class CardsRef {
        int type;
        int index;
    }

    static ArrayList<DataPictureCard> dataPictureCards;
    static ArrayList<DataBlogCard> dataBlogCards;
    static ArrayList<CardsRef> cardsRef;


    //View handlers
    LinearLayout layout;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    //Universal Image Loader variables
    ImageLoader imageLoader;
    DisplayImageOptions options;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataPictureCards = new ArrayList<>();
        dataBlogCards = new ArrayList<>();
        cardsRef = new ArrayList<>();
        //ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        //ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        requestPictures();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

   // Interface with Activity
    public interface OnFragmentInteractionListener {
        void  onListViewScrollStart();
        void  onListViewScrollStop();
        void onFragmentInteraction(Uri uri);
    }



    // Function to make http request for pictures. The received image urls are added to imageUrls.
    // During the first call the listView is initialized
    private void requestPictures () {
        Backend.getInstance().getCards(cardsRef.size(), Backend.getInstance().new GetCardsListener() {
            @Override
            public void onCardsFetched(ArrayList<DataPictureCard> dataPictureCards, ArrayList<DataBlogCard> dataBlogCards, ArrayList<CardsRef> cardsRef) {
                copyCards(dataPictureCards, dataBlogCards, cardsRef);
            }

            @Override
            public void onGetCardsFailed() {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_connect_server_failed, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void copyCards(ArrayList<DataPictureCard> dataPictureCards, ArrayList<DataBlogCard> dataBlogCards, ArrayList<CardsRef> cardsRef) {
        this.dataPictureCards.addAll(dataPictureCards);
        this.dataBlogCards.addAll(dataBlogCards);
        this.cardsRef.addAll(cardsRef);

        if (this.cardsRef.size() <= NO_OF_CARDS)
            initializeListView();
    }


    //Function to initialze the listView with  the adapter and the infinite scroll listener
    private void initializeListView () {

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        final ImageAdapter imageAdapter = new ImageAdapter(getActivity());
        ((RecyclerView) recyclerView).setAdapter(imageAdapter);


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                requestPictures();

                // update the adapter, saving the last known size
                int curSize = imageAdapter.getItemCount();

                // for efficiency purposes, only notify the adapter of what elements that got changed
                // curSize will equal to the index of the first element inserted because the list is 0-indexed
                imageAdapter.notifyItemRangeInserted(curSize, cardsRef.size() - 1);
            }

            @Override
            public void onScrollStart() {
                mListener.onListViewScrollStart();
            }

            @Override
            public void onScrollStop() {
                mListener.onListViewScrollStop();
            }

        });

    }



    //Adapter to populate listView with picture and blog cards
    public class ImageAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;


        ImageAdapter(Context context) {
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
            return cardsRef.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch(viewType) {
                case TYPE_PICTURE:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_picture, parent, false);
                    CardPictureViewHolder cardPictureViewHolder = new CardPictureViewHolder(view);
                    return cardPictureViewHolder;

                case TYPE_BLOG:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_blog, parent, false);
                    CardBlogViewHolder cardBlogViewHolder = new CardBlogViewHolder(view);
                    return cardBlogViewHolder;

                default:
                    return null;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {

                case TYPE_PICTURE:
                    CardPictureViewHolder cardPictureViewHolder = (CardPictureViewHolder) holder;

                    cardPictureViewHolder.description.setText(dataPictureCards.get(cardsRef.get(position).index).description);
                    //cardPictureViewHolder.likes.setText(String.valueOf(dataPictureCards.get(cardsRef.get(position).index).likes));
                    cardPictureViewHolder.activity.setText(dataPictureCards.get(cardsRef.get(position).index).activity);
                    cardPictureViewHolder.location.setText(dataPictureCards.get(cardsRef.get(position).index).location);
                    ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).link, cardPictureViewHolder.image, options, animateFirstListener);
                    //cardPictureViewHolder.uploader.name.setText(dataPictureCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_name);
                    //ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_pp, cardPictureViewHolder.uploader.pp, options, null);
                    ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_pp, cardPictureViewHolder.uploaderPic, options, null);

                    if (dataPictureCards.get(cardsRef.get(position).index).isLiked == false) {
                        cardPictureViewHolder.like_button.setVisibility(View.VISIBLE);
                        cardPictureViewHolder.location.setVisibility(View.GONE);
                        cardPictureViewHolder.addLikeCallback(dataPictureCards.get(cardsRef.get(position).index), this, position);
                    } else {
                        cardPictureViewHolder.like_button.setImageResource(R.drawable.ic_liked);
                        cardPictureViewHolder.add_to_bl_button.setVisibility(View.VISIBLE);
                        cardPictureViewHolder.location.setVisibility(View.VISIBLE);
                    }
                    break;

                case TYPE_BLOG:
                    CardBlogViewHolder cardBlogViewHolder = (CardBlogViewHolder) holder;
                    ImageLoader.getInstance().displayImage(dataBlogCards.get(cardsRef.get(position).index).thumbnail_url, cardBlogViewHolder.thumbnail, options, null);
                    cardBlogViewHolder.title.setText(dataBlogCards.get(cardsRef.get(position).index).title);
                    cardBlogViewHolder.extract.setText(dataBlogCards.get(cardsRef.get(position).index).extract);
                    //cardBlogViewHolder.like_button.setText("Like");
                    //cardBlogViewHolder.likes.setText(dataBlogCards.get(cardsRef.get(position).index).likes + " People Likes this");
                    cardBlogViewHolder.activity.setText("Diving");
                    cardBlogViewHolder.location.setText(dataBlogCards.get(cardsRef.get(position).index).location);
                    //cardBlogViewHolder.uploader.name.setText(dataBlogCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_name);
                    //ImageLoader.getInstance().displayImage(dataBlogCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_pp, cardBlogViewHolder.uploader.pp, options, null);
                    ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).dataUploaderBar.uploader_pp, cardBlogViewHolder.uploaderPic, options, null);


                    cardBlogViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataBlogCards.get(cardsRef.get(position).index).url));
                            getActivity().startActivity(browserIntent);
                        }
                    });

                    if (dataPictureCards.get(cardsRef.get(position).index).isLiked == false) {
                        cardBlogViewHolder.addLikeCallback(dataBlogCards.get(cardsRef.get(position).index), this, position);
                    }
            }
        }


        @Override
        public int getItemViewType(int position) {
            return cardsRef.get(position).type;
        }



    }

    static class BarUploaderViewHolder {
        TextView name;
        ImageView pp;
    }



    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 1000);
                    displayedImages.add(imageUri);
                }
            }
        }
    }





    public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int visibleThreshold = LOADING_THRESHOLD;
        private int currentPage = 0;
        private int previousTotalItemCount = 0;
        private boolean loading = true;
        private int startingPageIndex = 0;

        private LinearLayoutManager mLinearLayoutManager;

        public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLinearLayoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = view.getChildCount();
            int totalItemCount = mLinearLayoutManager.getItemCount();

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        public abstract void onScrollStart();
        public abstract void onScrollStop();

        @Override
        public void onScrollStateChanged (RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                onScrollStart();
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                onScrollStop();

        }
    }





}
