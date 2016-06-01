package travnet.discovery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Backend.getInstance().initialize(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        final ImageLoader imageLoader = ImageLoader.getInstance();

        SharedPreferences myPrefs = this.getSharedPreferences("login", MODE_PRIVATE);
        boolean isLogged = myPrefs.getBoolean("isLogged", false);
        String userID = myPrefs.getString("user_id", "");

        final Intent intent = new Intent(this, MainActivity.class);

        if (isLogged && !userID.equals("")) {
            Log.i("login", userID);
            User.getInstance().setUserID(userID);
            intent.putExtra("isLogged", true);
        } else {
            intent.putExtra("isLogged", false);
        }

        Backend.getInstance().getCards(0, Backend.getInstance().new GetCardsListener() {
            @Override
            public void onCardsFetched(ArrayList<DataPictureCard> dataPictureCards, ArrayList<DataBlogCard> dataBlogCards, ArrayList<HomeFragment.CardsRef> cardsRef) {
                for (int i=0; i<dataPictureCards.size(); i++) {
                    imageLoader.loadImageSync(dataPictureCards.get(i).link);
                }

                startActivity(intent);
                finish();
            }

            @Override
            public void onGetCardsFailed() {
                startActivity(intent);
                finish();
            }
        });

    }
}
