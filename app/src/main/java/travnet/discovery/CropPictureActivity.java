package travnet.discovery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;

public class CropPictureActivity extends AppCompatActivity {

    CropImageView cropImageView;
    Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cropImageView = (CropImageView) findViewById(R.id.crop_image_view);
        buttonDone = (Button) findViewById(R.id.button_done_cropping);

        String imagePath = getIntent().getExtras().getString("path");
        setImageForCropping(imagePath);

    }


    public Uri createSaveUri() {
        return Uri.fromFile(new File(this.getCacheDir(), "croppedImage"));
    }

    public void setImageForCropping(String imagePath) {
        //Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
        File imageFile = new File(imagePath);
        cropImageView.startLoad(Uri.fromFile(imageFile), new LoadCallback() {
            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), R.string.error_get_picture_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                buttonDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cropImageView.startCrop(createSaveUri(),
                                new CropCallback() {
                                    @Override
                                    public void onSuccess(Bitmap croppedImage) {

                                    }

                                    @Override
                                    public void onError() {
                                    }
                                },

                                new SaveCallback() {
                                    @Override
                                    public void onSuccess(Uri outputUri) {
                                        //Process cropped image

                                    }

                                    @Override
                                    public void onError() {
                                    }
                                }
                        );
                    }
                });
            }

        });
    }


}
