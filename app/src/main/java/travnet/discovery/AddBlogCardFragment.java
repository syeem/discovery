package travnet.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBlogCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBlogCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBlogCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String blogURL;
    String blogTitle;
    String blogExtract;
    Bitmap blogThumbnail;

    EditText inputBlogLink;
    Button buttonPreviewBlog;
    TextView previewTitle;
    TextView previewExtract;
    ImageView previewThumbnail;
    Button buttonPostBlog;

    private OnFragmentInteractionListener mListener;

    public AddBlogCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBlogCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBlogCardFragment newInstance(String param1, String param2) {
        AddBlogCardFragment fragment = new AddBlogCardFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_blog_card, container, false);

        inputBlogLink = (EditText) view.findViewById(R.id.blog_link);
        buttonPreviewBlog = (Button) view.findViewById(R.id.button_preview_blog);
        previewTitle = (TextView) view.findViewById(R.id.title);
        previewExtract = (TextView) view.findViewById(R.id.extract);
        previewThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        buttonPostBlog = (Button) view.findViewById(R.id.button_post_blog);

        buttonPreviewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogURL = inputBlogLink.getText().toString();
                TextCrawler textCrawler = new TextCrawler();
                textCrawler.makePreview(linkPreviewCallback, blogURL);
            }
        });

        buttonPostBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBlog();
            }
        });



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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {

        @Override
        public void onPre() {
        }

        @Override
        public void onPos(SourceContent sourceContent, boolean b) {
            blogTitle = sourceContent.getTitle();
            blogExtract = sourceContent.getDescription();

            previewTitle.setText(blogTitle);
            previewExtract.setText(blogExtract);
            String thumbnailUrl = sourceContent.getImages().get(0);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(thumbnailUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    blogThumbnail = loadedImage;
                    previewThumbnail.setImageBitmap(loadedImage);
                }
            });
            //ImageLoader.getInstance().displayImage(thumbnailUrl, previewThumbnail);
        }
    };



    public void postBlog() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://54.86.18.174/api/postBlog";
        String image = getStringImage(blogThumbnail);

        JSONObject blog = new JSONObject();
        try {
            blog.put("card-type", "blog");
            blog.put("url", blogURL);
            blog.put("title", blogTitle);
            blog.put("extract", blogExtract);
            blog.put("thumbnail", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, blog, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.error_connect_server_failed, Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsObjRequest);
    }











    /*public void postBlog() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://54.86.18.174/api/postBlog";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_connect_server_failed, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("card-type", "blog");
                params.put("url", blogURL);
                params.put("title", blogTitle);
                params.put("extract", blogExtract);
                String image = getStringImage(blogThumbnail);
                params.put("thumbnail", image);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }*/

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



}
