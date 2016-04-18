package travnet.discovery;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.nostra13.universalimageloader.core.ImageLoader;


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

    String testURL = "http://www.alexinwanderland.com/2016/03/09/antelope-canyon-lake-powell/";
    TextView title;
    TextView extract;
    ImageView thumbnail;


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

        title = (TextView) view.findViewById(R.id.title);
        extract = (TextView) view.findViewById(R.id.extract);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);


        TextCrawler textCrawler = new TextCrawler();
        textCrawler.makePreview(linkPreviewCallback, testURL);

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
            title.setText(sourceContent.getTitle());
            extract.setText(sourceContent.getDescription());
            String thumbnailUrl = sourceContent.getImages().get(0);
            ImageLoader.getInstance().displayImage(thumbnailUrl, thumbnail);

        }
    };

}
