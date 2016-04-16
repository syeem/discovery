package travnet.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddPictureCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddPictureCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPictureCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText add_location;
    AutoCompleteTextView activityAutoCompleteView;
    String[] activityList = { "Sightseeing", "Surfing", "Trekking", "Yoga"};

    private OnFragmentInteractionListener mListener;

    public AddPictureCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPictureCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPictureCardFragment newInstance(String param1, String param2) {
        AddPictureCardFragment fragment = new AddPictureCardFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_picture_card, container, false);

        ImageView previewImage = (ImageView) view.findViewById(R.id.previewImage);
        Parcelable parcelable = getArguments().getParcelable("uri");
        Uri uri = (Uri) parcelable;
        previewImage.setImageURI(uri);

        add_location = (EditText) view.findViewById(R.id.add_location);
        add_location.setOnClickListener(getLocation);

        activityAutoCompleteView = (AutoCompleteTextView) view.findViewById(R.id.add_activity);
        ArrayAdapter<String> activityAdapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item, activityList);
        activityAutoCompleteView.setAdapter(activityAdapter);

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    EditText.OnClickListener getLocation = new EditText.OnClickListener() {
       @Override
        public void onClick(View v) {
           try {
               Intent intent =
                       new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                               .build(getActivity());
               startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
           } catch (GooglePlayServicesRepairableException e) {
               Toast.makeText(getActivity().getApplicationContext(), R.string.error_get_location_failed, Toast.LENGTH_LONG).show();
           } catch (GooglePlayServicesNotAvailableException e) {
               Toast.makeText(getActivity().getApplicationContext(), R.string.error_get_location_failed, Toast.LENGTH_LONG).show();
           }
        }
    };




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                add_location.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
