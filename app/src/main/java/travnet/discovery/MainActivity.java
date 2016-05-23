package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.*;
import com.facebook.login.LoginFragment;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class MainActivity extends AppCompatActivity
        implements travnet.discovery.LoginFragment.OnFragmentInteractionListener, travnet.discovery.LoginFragment.OnLoginListener,
        PictureFragment.OnFragmentInteractionListener,
        ProfileInfoFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {

    PictureFragment pictureFragment;

    View navDrawerheader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navDrawerheader = navigationView.getHeaderView(0);

        //Floating action buttons
        FloatingActionButton fabAddBlog = (FloatingActionButton) findViewById(R.id.fab_add_blog);
        fabAddBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBlogCard();
            }
        });
        FloatingActionButton fabAddPictureGallery = (FloatingActionButton) findViewById(R.id.fab_add_picture_gallery);
        fabAddPictureGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPictureCardGallery();
            }
        });
        FloatingActionButton fabAddPictureCamera = (FloatingActionButton) findViewById(R.id.fab_add_picture_camera);
        fabAddPictureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPictureCardCamera();
            }
        });


        FacebookSdk.sdkInitialize(this);
        Backend.getInstance().initialize(getApplicationContext());

        pictureFragment = new PictureFragment();

        //Check for previous login
        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        //boolean isLogged = myPrefs.getBoolean("isLogged", false);
        boolean isLogged = false;
        if (isLogged) {
            String userID = myPrefs.getString("user_id", "");
            User.getInstance().setUserID(userID);

            //Set Picture Fragment
            pictureFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, pictureFragment).commit();

            Backend.getInstance().getUserInfo(Backend.getInstance().new GetUserInfoListener() {
                @Override
                public void onUserInfoFetched() {
                    Toast.makeText(getApplicationContext(), "User info fetched", Toast.LENGTH_LONG).show();
                }
            });


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

    }

    public void onUploadingBlog() {


    }


    public void onImageCropped(Uri uri) {

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


    void addBlogCard() {
        Intent intent = new Intent(this, AddBlogCardActivity.class);
        this.startActivity(intent);
    }

    void addPictureCardGallery() {
        Intent intent = new Intent(this, AddPictureCardActivity.class);
        intent.putExtra("source", "gallery");
        this.startActivity(intent);
    }

    void addPictureCardCamera() {
        Intent intent = new Intent(this, AddPictureCardActivity.class);
        intent.putExtra("source", "camera");
        this.startActivity(intent);
    }


    void updateNavDrawerHeader() {
        ImageView profilePic = (ImageView) navDrawerheader.findViewById(R.id.profile_pic);
        profilePic.setImageBitmap(User.getInstance().getProfilePic());
        TextView profileName = (TextView) navDrawerheader.findViewById(R.id.profile_name);
        profileName.setText(User.getInstance().getName());
        TextView profileHome = (TextView) navDrawerheader.findViewById(R.id.profile_home);
        profileHome.setText(User.getInstance().getHometown());
    }

}
