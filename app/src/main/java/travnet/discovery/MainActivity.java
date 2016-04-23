package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.*;
import com.facebook.login.LoginFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity
        implements travnet.discovery.LoginFragment.OnFragmentInteractionListener, travnet.discovery.LoginFragment.OnLoginListener,
        PictureFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        UploadFragment.OnFragmentInteractionListener,
        BucketListFragment.OnFragmentInteractionListener,
        CropPictureFragment.OnFragmentInteractionListener,
        AddPictureCardFragment.OnFragmentInteractionListener,
        AddBlogCardFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    ProfileFragment profileFragment;
    PictureFragment pictureFragment;
    UploadFragment uploadFragment;
    BucketListFragment bucketListFragment;
    CropPictureFragment cropPictureFragment;

    //private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /*mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage()
                .build();*/

        FacebookSdk.sdkInitialize(this);

        profileFragment = new ProfileFragment();
        pictureFragment = new PictureFragment();
        uploadFragment = new UploadFragment();
        bucketListFragment = new BucketListFragment();

        //Check for previous login
        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogged = myPrefs.getBoolean("isLogged", false);
        //boolean isLogged = false;
        if (isLogged){
            //Set Picture Fragment
            pictureFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, pictureFragment).commit();
            addToolbar();
        } else {
            //Set Login fragment
            travnet.discovery.LoginFragment loginFragment = new travnet.discovery.LoginFragment();
            loginFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment).commit();
        }

    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void onListViewScrollStart(){
        toolbar.animate().translationY(toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void onListViewScrollStop(){
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    public void onLoginSuccessful(){
        //Save login
        SharedPreferences myPrefs = MainActivity.this.getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean("isLogged", true);
        prefsEditor.commit();

        //Replace Login Fragment with Picture Fragment
        replaceFragment(pictureFragment);
        addToolbar();
    }

    public void replaceFragment (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void addToolbar () {
        View navigation = getLayoutInflater().inflate(R.layout.bar_navigation, null);
        toolbar.addView(navigation);

        Button buttonProfile = (Button)navigation.findViewById(R.id.profile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(profileFragment);
            }
        });

        Button buttonHome = (Button)navigation.findViewById(R.id.home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(pictureFragment);
            }
        });

        Button buttonUpload = (Button)navigation.findViewById(R.id.upload);
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(uploadFragment);
            }
        });

        Button buttonBucketList = (Button)navigation.findViewById(R.id.bucket_list);
        buttonBucketList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(bucketListFragment);
            }
        });

    }

    public  void onPictureSelected(String imagePath) {
        Bundle bundle = new Bundle();
        bundle.putString("path", imagePath);
        cropPictureFragment = new CropPictureFragment();
        cropPictureFragment.setArguments(bundle);
        replaceFragment(cropPictureFragment);
    }

    public void onUploadingBlog() {
        AddBlogCardFragment addBlogCardFragment = new AddBlogCardFragment();
        replaceFragment(addBlogCardFragment);
    }


    public void onImageCropped(Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("uri", uri);
        AddPictureCardFragment addPictureCardFragment = new AddPictureCardFragment();
        addPictureCardFragment.setArguments(bundle);
        replaceFragment(addPictureCardFragment);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
