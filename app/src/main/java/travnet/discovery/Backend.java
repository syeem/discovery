package travnet.discovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 5/20/16.
 */
public class Backend {
    private static Backend ourInstance = new Backend();
    private Context context;

    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {
    }

    public void initialize(Context context) {
        this.context = context;
    }


    enum Task
    {
        GET_USER_INFO,
        REGISTER_NEW_USER
    }

    public void registerNewUser(final String id, final String name, final String email, final String location, final String hometown, final String ppURL) {

        class RegisterNewUserTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                //Bitmap profilePic = ImageLoader.getInstance().loadImageSync(ppURL);
                Bitmap profilePic = getBitmapFromURL(ppURL);
                User.getInstance().setProfilePic(profilePic);


                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://54.86.18.174/api/registerUser";
                String image = encodeImage(profilePic);

                JSONObject user = new JSONObject();
                try {
                    user.put("email", email);
                    user.put("name", name);
                    user.put("living_in", location);
                    user.put("home", hometown);
                    user.put("facebook_id", id);
                    user.put("date_of_birth", "25");
                    user.put("profile_pic", ppURL);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, user, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //Stub
                                String userID = null;
                                try {
                                    userID = response.getString("user_id");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                User.getInstance().setUserID(userID);
                                SharedPreferences myPrefs = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = myPrefs.edit();
                                prefsEditor.putString("user_id", User.getInstance().getUserID());
                                prefsEditor.commit();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });


                queue.add(jsObjRequest);

                return null;
            }

            protected Void onPostExecute() {
                return null;
            }
        }

        new RegisterNewUserTask().execute();

    }


    public abstract  class GetUserInfoListener {
        public GetUserInfoListener() {
        }

        public abstract void onUserInfoFetched();
    }


    public void getUserInfo(final GetUserInfoListener listener) {

        class getUserInfoTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://54.86.18.174/api/getUserInfo";

                JSONObject userID = new JSONObject();
                try {
                    userID.put("user_id", User.getInstance().getUserID());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, userID, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v("xyz", "jdhf");

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                queue.add(jsObjRequest);

                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                listener.onUserInfoFetched();
            }



        }

        new getUserInfoTask().execute();

    }





    public String encodeImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



}
