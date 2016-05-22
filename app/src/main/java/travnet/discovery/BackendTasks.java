package travnet.discovery;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static travnet.discovery.Backend.getBitmapFromURL;

/**
 * Created by Sunny on 22/5/2016.
 */
public class BackendTasks {

    public Context context;
    public GetUserInfo userInfo;
    public RegisterUser registerUser;

    public BackendTasks()
    {
        this.userInfo = new GetUserInfo();
        this.registerUser = new RegisterUser();
    }

    public class GetUserInfo extends AsyncTask<Void, Void, Void> {

        void updateUserInfo(){
        }

        @Override
        protected void onPostExecute(Void value) {
            updateUserInfo();
        }

        @Override
        protected Void doInBackground(Void... params) {

            RequestQueue queue = Volley.newRequestQueue(context);
            String url = "http://192.168.1.25:8080/api/getUserInfo";

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

    }






    class RegisterUser extends AsyncTask<User, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean value) {
            return;
        }

        @Override
        protected Boolean doInBackground(User... params) {
            HandleRegisterUser();
            return true;
        }
    }

    private void HandleGetUserInfo()
    {

    }

    private void HandleRegisterUser()
    {

    }
}
