package travnet.discovery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CropPictureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CropPictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CropPictureFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CropImageView cropImageView;
    Button buttonDone;

    private OnFragmentInteractionListener mListener;

    public CropPictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CropPictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CropPictureFragment newInstance(String param1, String param2) {
        CropPictureFragment fragment = new CropPictureFragment();
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
        View view = inflater.inflate(R.layout.fragment_crop_picture, container, false);
        cropImageView = (CropImageView) view.findViewById(R.id.crop_image_view);
        buttonDone = (Button) view.findViewById(R.id.button_done_cropping);

        String imagePath = getArguments().getString("path");
        setImageForCropping(imagePath);
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
        void onFragmentInteraction(Uri uri);
        void onImageCropped(Uri uri);
    }


    public Uri createSaveUri() {
        return Uri.fromFile(new File(getActivity().getCacheDir(), "croppedImage"));
    }

    public void setImageForCropping(String imagePath) {
        //Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
        File imageFile = new File(imagePath);
        cropImageView.startLoad(Uri.fromFile(imageFile), new LoadCallback() {
            @Override
            public void onError() {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_get_picture_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                buttonDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cropImageView.startCrop(createSaveUri(),
                                new CropCallback() {
                                    @Override
                                    public void onSuccess(Bitmap croppedImage) {

                                    }

                                    @Override
                                    public void onError() {
                                    }
                                },

                                new SaveCallback() {
                                    @Override
                                    public void onSuccess(Uri outputUri) {
                                        mListener.onImageCropped(outputUri);
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                }
                        );
                    }
                });
            }

        });
    }


}
