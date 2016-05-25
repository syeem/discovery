package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.FacebookSdk;
import com.facebook.login.*;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity
        implements SignInFragment.OnFragmentInteractionListener, SignInFragment.OnLoginListener,
        HomeFragment.OnFragmentInteractionListener,
        GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener {

    HomeFragment homeFragment;
    View navDrawerheader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(this);
        Backend.getInstance().initialize(getApplicationContext());
        homeFragment = new HomeFragment();


        //Check for previous login
        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogged = myPrefs.getBoolean("isLogged", false);
        //boolean isLogged = false;
        if (isLogged) {
            String userID = myPrefs.getString("user_id", "");
            User.getInstance().setUserID(userID);

            //Set Picture Fragment
            homeFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment).commit();

            setupHomeScreen();

        } else {
            //Set Login fragment
            SignInFragment signInFragment = new SignInFragment();
            signInFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, signInFragment).commit();
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
        replaceFragment(homeFragment);
        setupHomeScreen();
    }

    void setupHomeScreen() {
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


        Backend.getInstance().getUserInfo(Backend.getInstance().new GetUserInfoListener() {
            @Override
            public void onUserInfoFetched() {
                updateNavDrawerHeader();
                Toast.makeText(getApplicationContext(), "User info fetched", Toast.LENGTH_LONG).show();
            }
        });


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
            LoginManager.getInstance().logOut();
            //Clear SharedPreferences
            SharedPreferences myPrefs = MainActivity.this.getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = myPrefs.edit();
            prefsEditor.putBoolean("isLogged", false);
            prefsEditor.commit();
            //Restart activity
            Intent intent = getIntent();
            finish();
            startActivity(intent);
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
