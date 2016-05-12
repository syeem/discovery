package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.facebook.FacebookSdk;
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
        UploadFragment.OnFragmentInteractionListener,
        ProfileInfoFragment.OnFragmentInteractionListener,
        CropPictureFragment.OnFragmentInteractionListener,
        AddPictureCardFragment.OnFragmentInteractionListener,
        AddBlogCardFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {

    PictureFragment pictureFragment;
    UploadFragment uploadFragment;
    CropPictureFragment cropPictureFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Floating Action Button
        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        FacebookSdk.sdkInitialize(this);

        pictureFragment = new PictureFragment();
        uploadFragment = new UploadFragment();

        //Check for previous login
        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogged = myPrefs.getBoolean("isLogged", false);
        //boolean isLogged = false;
        if (isLogged){
            //Set Picture Fragment
            pictureFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, pictureFragment).commit();
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
    }

    public void onListViewScrollStop(){
    }

    public void onLoginSuccessful(){
        //Save login
        SharedPreferences myPrefs = MainActivity.this.getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean("isLogged", true);
        prefsEditor.commit();

        //Replace Login Fragment with Picture Fragment
        replaceFragment(pictureFragment);
    }

    public void replaceFragment (Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_photos) {
            Intent intent = new Intent(this, PicturesActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_profile_interests) {
            Intent intent = new Intent(this, InterestActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.nav_profile_bucket_list) {
            Intent intent = new Intent(this, BucketListActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
