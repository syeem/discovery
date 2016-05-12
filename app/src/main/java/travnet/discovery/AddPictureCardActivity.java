package travnet.discovery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class AddPictureCardActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String source = getIntent().getExtras().getString("source");
        if (source.equals("gallery")) {
            browsePhoneGallery();
        } else if (source.equals("camera")) {
            openCamera();
        }


    }

    public void openCamera() {
        EasyImage.openCamera(this, 2);
    }

    public void browsePhoneGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }

        EasyImage.openGallery(this, 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Toast.makeText(getApplicationContext(), R.string.error_get_picture_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                cropPicture(imageFile.getAbsolutePath());
            }
        });
    }

    void cropPicture(String path) {
        Intent intent = new Intent(this, CropPictureActivity.class);
        intent.putExtra("path", path);
        this.startActivity(intent);
    }




}
