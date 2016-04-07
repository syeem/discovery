package travnet.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PictureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int LOADING_THRESHOLD = 2;
    public static final int NO_OF_CARDS = 5;
    public static final String URL_GET_STRING = "http://54.86.18.174/api/getCards";

    private static final int TYPE_PICTURE = 0;
    private static final int TYPE_BLOG = 1;


    //Data Structure for picture cards and blog cards
    public class DataPictureCard {
        String description;
        String link;
        String likes;
        String location;
        String activity;
        String uploader_name;
        String uploader_pp;
    }

    public class DataBlogCard {
        String thumbnail_url;
        String title;
        String extract;
        String likes;
        String location;
        String uploader_name;
        String uploader_pp;
    }

    public class CardsRef {
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureFragment newInstance(String param1, String param2) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Initialize
        dataPictureCards = new ArrayList<>();
        dataBlogCards = new ArrayList<>();
        cardsRef = new ArrayList<>();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        requestPictures();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void  onListViewScrollStart();
        void  onListViewScrollStop();
        void onFragmentInteraction(Uri uri);
    }


    // Function to make http request for pictures. The received image urls are added to imageUrls.
    // During the first call the listView is initialized
    private void requestPictures () {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = URL_GET_STRING;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray arrayJson = jsonObj.getJSONArray("cards");
                            for (int i=0; i< NO_OF_CARDS; i++) {
                                JSONObject card = arrayJson.getJSONObject(i);
                                CardsRef cardRef = new CardsRef();

                                String check = card.getString(("card-type"));
                                if (card.getString("card-type").equals("image")) {
                                    cardRef.type = TYPE_PICTURE;
                                    cardRef.index = dataPictureCards.size();
                                    cardsRef.add(cardRef);
                                    JSONObject content = card.getJSONObject("content");
                                    DataPictureCard temp = new DataPictureCard();
                                    temp.description = content.getString("description");
                                    temp.link = content.getString("url");
                                    temp.likes = card.getString("likes");
                                    temp.location = card.getString("location");
                                    temp.activity = "Place holder"; /*card.getString("description");*/
                                    temp.uploader_name = card.getString("user-name");
                                    temp.uploader_pp = card.getString("user-img");
                                    dataPictureCards.add(temp);
                                }
                                else if (card.getString("card-type").equals("blog")) {
                                    cardRef.type = TYPE_BLOG;
                                    cardRef.index = dataBlogCards.size();
                                    cardsRef.add(cardRef);
                                    JSONObject content = card.getJSONObject("content");
                                    DataBlogCard temp = new DataBlogCard();
                                    temp.thumbnail_url = content.getString("thumbnail");
                                    temp.title = content.getString("title");
                                    temp.extract = content.getString("abstract");
                                    temp.likes = card.getString("likes");
                                    temp.location = card.getString("location");
                                    temp.uploader_name = card.getString("user-name");
                                    temp.uploader_pp = card.getString("user-img");
                                    dataBlogCards.add(temp);
                                }

                            }
                            if (cardsRef.size() <= NO_OF_CARDS)
                                initializeListView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_connect_server_failed, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
    private static class ImageAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private DisplayImageOptions options;


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

            }
        }


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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {

                case TYPE_PICTURE:
                    CardPictureViewHolder cardPictureViewHolder = (CardPictureViewHolder) holder;

                    cardPictureViewHolder.like_button.setText("Like");
                    cardPictureViewHolder.add_to_bl_button.setText("Bucket");

                    cardPictureViewHolder.description.setText(dataPictureCards.get(cardsRef.get(position).index).description);
                    cardPictureViewHolder.likes.setText(dataPictureCards.get(cardsRef.get(position).index).likes + " People Likes this");
                    cardPictureViewHolder.activity.setText(dataPictureCards.get(cardsRef.get(position).index).activity);
                    cardPictureViewHolder.location.setText(dataPictureCards.get(cardsRef.get(position).index).location);
                    ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).link, cardPictureViewHolder.image, options, animateFirstListener);
                    cardPictureViewHolder.uploader.name.setText(dataPictureCards.get(cardsRef.get(position).index).uploader_name);
                    ImageLoader.getInstance().displayImage(dataPictureCards.get(cardsRef.get(position).index).uploader_pp, cardPictureViewHolder.uploader.pp, options, null);
                    break;

                case TYPE_BLOG:
                    CardBlogViewHolder cardBlogViewHolder = (CardBlogViewHolder) holder;
                    ImageLoader.getInstance().displayImage(dataBlogCards.get(cardsRef.get(position).index).thumbnail_url, cardBlogViewHolder.thumbnail, options, null);
                    cardBlogViewHolder.title.setText(dataBlogCards.get(cardsRef.get(position).index).title);
                    cardBlogViewHolder.extract.setText(dataBlogCards.get(cardsRef.get(position).index).extract);
                    cardBlogViewHolder.likes.setText(dataBlogCards.get(cardsRef.get(position).index).likes + " People Likes this");
                    cardBlogViewHolder.activity.setText("Place holder activity");
                    cardBlogViewHolder.location.setText(dataBlogCards.get(cardsRef.get(position).index).location);
                    cardBlogViewHolder.uploader.name.setText(dataBlogCards.get(cardsRef.get(position).index).uploader_name);
                    ImageLoader.getInstance().displayImage(dataBlogCards.get(cardsRef.get(position).index).uploader_pp, cardBlogViewHolder.uploader.pp, options, null);

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
