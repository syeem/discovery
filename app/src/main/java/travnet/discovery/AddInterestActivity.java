package travnet.discovery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AddInterestActivity extends AppCompatActivity {

    ArrayList<String> selectedIntersets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedIntersets = new ArrayList<>();
        //Stub
        selectedIntersets.add("New Interest");

        Button buttonAdd = (Button) findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnInterests();
            }
        });

    }

    private void returnInterests() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("interests", selectedIntersets);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
