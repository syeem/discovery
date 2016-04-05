package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.facebook.login.*;
import com.facebook.login.LoginFragment;

public class MainActivity extends AppCompatActivity
        implements travnet.discovery.LoginFragment.OnFragmentInteractionListener, travnet.discovery.LoginFragment.OnLoginListener, PictureFragment.OnFragmentInteractionListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Check for previous login
        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogged = myPrefs.getBoolean("isLogged", false);
        if (isLogged){
            //Set Picture Fragment
            PictureFragment pictureFragment = new PictureFragment();
            pictureFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, pictureFragment).commit();
            toolbar.inflateMenu(R.menu.menu_navigation);

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
        PictureFragment pictureFragment = new PictureFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, pictureFragment);
        transaction.commit();
        toolbar.inflateMenu(R.menu.menu_navigation);
    }

}
