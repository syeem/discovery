package travnet.discovery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class AddBlogCardActivity extends AppCompatActivity {

    String blogURL;
    String blogTitle;
    String blogExtract;
    Bitmap blogThumbnail;

    EditText inputBlogLink;
    Button buttonPreviewBlog;
    TextView previewTitle;
    TextView previewExtract;
    ImageView previewThumbnail;
    Button buttonPostBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputBlogLink = (EditText) findViewById(R.id.blog_link);
        buttonPreviewBlog = (Button) findViewById(R.id.button_preview_blog);
        previewTitle = (TextView) findViewById(R.id.title);
        previewExtract = (TextView) findViewById(R.id.extract);
        previewThumbnail = (ImageView) findViewById(R.id.thumbnail);
        buttonPostBlog = (Button) findViewById(R.id.button_post_blog);

        buttonPreviewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogURL = inputBlogLink.getText().toString();
                TextCrawler textCrawler = new TextCrawler();
                textCrawler.makePreview(linkPreviewCallback, blogURL);
            }
        });

        buttonPostBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postBlog();
            }
        });

    }

    private LinkPreviewCallback linkPreviewCallback = new LinkPreviewCallback() {

        @Override
        public void onPre() {
        }

        @Override
        public void onPos(SourceContent sourceContent, boolean b) {
            blogTitle = sourceContent.getTitle();
            blogExtract = sourceContent.getDescription();

            previewTitle.setText(blogTitle);
            previewExtract.setText(blogExtract);
            String thumbnailUrl = sourceContent.getImages().get(0);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.loadImage(thumbnailUrl, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    blogThumbnail = loadedImage;
                    previewThumbnail.setImageBitmap(loadedImage);
                }
            });
            //ImageLoader.getInstance().displayImage(thumbnailUrl, previewThumbnail);
        }
    };



    public void postBlog() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://54.86.18.174/api/postBlog";
        String image = getStringImage(blogThumbnail);

        JSONObject blog = new JSONObject();
        try {
            blog.put("card-type", "blog");
            blog.put("url", blogURL);
            blog.put("title", blogTitle);
            blog.put("extract", blogExtract);
            blog.put("thumbnail", image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, blog, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_connect_server_failed, Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsObjRequest);
    }


    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
