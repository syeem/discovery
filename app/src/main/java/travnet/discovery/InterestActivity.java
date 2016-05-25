package travnet.discovery;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterestActivity extends AppCompatActivity {

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
        initializeLiztView();


        Backend.getInstance().getUserIntersets(Backend.getInstance().new GetUserInterestsListener() {
            @Override
            public void onUserInterestsFetched() {
                userInterests = User.getInstance().getInterests();
                populateListView();
            }
        });

    }


    private void initializeLiztView() {
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
        adapter = new ArrayAdapter<String>(this, R.layout.card_interest, userInterests);
        listInterests.setAdapter(adapter);
    }


}
