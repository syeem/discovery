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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by root on 5/20/16.
 */
public class Backend {
    private static Backend ourInstance = new Backend();
    private Context context;

    //private String baseUrl = "http://192.168.1.25:8080/api/";
    private String baseUrl = "http://54.86.18.174/api/";

    public static Backend getInstance() {
        return ourInstance;
    }

    private Backend() {
    }

    public void initialize(Context context) {
        this.context = context;
    }


    public abstract  class RegisterNewUserListener {
        public RegisterNewUserListener() {
        }

        public abstract void registerNewUserCompleted();
        public abstract void registerNewUserFailed();
    }

    public void registerNewUser(final String id, final String name, final String email, final String location, final String hometown, final String ppURL, final RegisterNewUserListener listener) {

        class RegisterNewUserTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {
                //Bitmap profilePic = ImageLoader.getInstance().loadImageSync(ppURL);
                Bitmap profilePic = getBitmapFromURL(ppURL);
                User.getInstance().setProfilePic(profilePic);


                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl + "registerUser";
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
                                listener.registerNewUserCompleted();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.registerNewUserFailed();
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
                String url = baseUrl + "getUserInfo";

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
                                listener.onUserInfoFetched();

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
            }



        }

        new getUserInfoTask().execute();

    }




    public abstract  class GetUserInterestsListener {
        public GetUserInterestsListener() {
        }

        public abstract void onUserInterestsFetched();
    }

    public void getUserIntersets(final GetUserInterestsListener listener) {
        class getUserInterestsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl + "getUserInterests";

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
                //Stub
                //User.getInstance().setInterests(Arrays.asList("Surfing"));
                listener.onUserInterestsFetched();

            }



        }

        new getUserInterestsTask().execute();

    }




    public abstract  class GetUserPicturesListener {
        public GetUserPicturesListener() {
        }

        public abstract void onUserPicturesFetched(ArrayList<DataPictureCard> userPictures);
    }

    public void getUserPictures(final GetUserPicturesListener listener) {
        class getUserPicturesTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl + "getUserPictures";

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
                //Stub
                String imageLink = "http://www.planwallpaper.com/static/images/4-Nature-Wallpapers-2014-1_ukaavUI.jpg";

                ArrayList userPictures = new ArrayList<DataPictureCard>();
                DataPictureCard data1 = new DataPictureCard("A", imageLink, 10, "D", "E", "F", "G");
                DataPictureCard data2 = new DataPictureCard("A", imageLink, 10, "D", "E", "F", "G");
                DataPictureCard data3 = new DataPictureCard("A", imageLink, 10, "D", "E", "F", "G");
                DataPictureCard data4 = new DataPictureCard("A", imageLink, 10, "D", "E", "F", "G");
                userPictures.add(data1);
                userPictures.add(data2);
                userPictures.add(data3);
                userPictures.add(data4);
                listener.onUserPicturesFetched(userPictures);

            }



        }

        new getUserPicturesTask().execute();

    }



    public abstract  class GetInterestsListener {
        public GetInterestsListener() {
        }

        public abstract void onInterestsFetched(ArrayList<String> listInterests);
    }

    public void getIntersets(final GetInterestsListener listener) {
        class getIntersetsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl + "getInterests";

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v("fd", "df");
                                ArrayList<String> listInterest = new ArrayList<String>();
                                try {
                                    JSONArray arrayJson = response.getJSONArray("interests");
                                    for (int i=0; i<arrayJson.length(); i++) {
                                        String interestName = arrayJson.getJSONObject(i).getString("interest");
                                        listInterest.add(interestName);
                                    }
                                    listener.onInterestsFetched(listInterest);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("fd", "df");
                            }
                        });

                queue.add(jsObjRequest);

                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
            }



        }

        new getIntersetsTask().execute();

    }




    public void updateUserInterests() {
        class updateUserInterestsTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl + "updateInterests";

                JSONObject userInterest = new JSONObject();
                try {
                    userInterest.put("user_id", User.getInstance().getUserID());
                    ArrayList<String> listInterest = new ArrayList<String>(User.getInstance().getInterests());
                    userInterest.put("interests", listInterest);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, userInterest, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v("abc", "ad");
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v("sa","fd");
                            }
                        });

                queue.add(jsObjRequest);

                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
            }

        }

        new updateUserInterestsTask().execute();

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
