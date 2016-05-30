package travnet.discovery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterestActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_INTEREST = 1;

    List<String> userInterests;
    SwipeMenuListView listInterests;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userInterests = new ArrayList<String>();
        listInterests = (SwipeMenuListView) findViewById(R.id.list_interests);

        initializeListView();
        userInterests.addAll(User.getInstance().getInterests());
        populateListView();

        /*Backend.getInstance().getUserIntersets(Backend.getInstance().new GetUserInterestsListener() {
            @Override
            public void onUserInterestsFetched() {
                userInterests = User.getInstance().getInterests();
                populateListView();
            }
        });*/

        Button buttonAddInterest = (Button) findViewById(R.id.add_interest);
        buttonAddInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddInterestActivity();
            }
        });

    }

    private void startAddInterestActivity() {
        Intent intent = new Intent(this, AddInterestActivity.class);
        intent.putExtra("interests", (Serializable) userInterests);
        this.startActivityForResult(intent, REQUEST_ADD_INTEREST);
    }


    private void initializeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create delete button
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0x00,0x00, 0x00)));
                deleteItem.setWidth(200);
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        listInterests.setMenuCreator(creator);
        listInterests.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listInterests.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        userInterests.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }


    private void populateListView() {
        adapter = new ArrayAdapter<String>(this, R.layout.card_interest, R.id.interest_name, userInterests);
        listInterests.setAdapter(adapter);
    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_INTEREST) {
            ArrayList<String> interestsToAdd = (ArrayList<String>) data.getSerializableExtra("interests");
            for (int i = 0; i < interestsToAdd.size(); i++) {
                userInterests.add(interestsToAdd.get(i));
                adapter.notifyDataSetChanged();
            }

        }

    }


}
