package md.jcarcamo.pickaplace;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.jcarcamo.pickaplace.utils.FirebasePoll;
import md.jcarcamo.pickaplace.utils.Restaurant;
import md.jcarcamo.pickaplace.utils.RestaurantsList;

public class PollActivity extends BaseActivity  {

    private static final String TAG = "PollActivity";

    private List<Restaurant> restaurants;
    private String pollId;
    private DatabaseReference pollRef;
    private int position;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.placeNameTextView)
    TextView placeName;
    @BindView(R.id.placeSummaryTextView)
    TextView placeSummary;

    @BindView(R.id.imageView)
    ImageView restaurantPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants = new ArrayList<>();
        setContentView(R.layout.activity_poll);
        ButterKnife.bind(this);
        pollId = getIntent().getStringExtra("pollId");
        Log.d(TAG,"Poll Id: "+pollId);
        pollRef = FirebaseDatabase.getInstance().getReference("poll/"+pollId).child("restaurants");
        pollRef.addChildEventListener (chEvListener);
        position = 0;
        showProgressDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onResume(){
        super.onResume();
        pollRef = FirebaseDatabase.getInstance().getReference("poll"+pollId).child("restaurants");
        pollRef.addChildEventListener (chEvListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        restaurants.clear();
        pollRef.removeEventListener(chEvListener);
    }

    private ChildEventListener chEvListener = new ChildEventListener() {
        private  boolean firstLoaded = false;
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG,"Initializing restaurants");
            Restaurant tmpRestaurant = dataSnapshot.getValue(Restaurant.class);
            tmpRestaurant.setId(dataSnapshot.getKey());
            restaurants.add(tmpRestaurant);
            if(!firstLoaded) {
                updateUI(tmpRestaurant);
            }
            firstLoaded = true;
            //FirebasePoll fbp = (FirebasePoll)dataSnapshot.getValue(FirebasePoll.class);
            //restaurants = fbp.getRestaurants();
            Log.d(TAG,"restaurants loaded: " + restaurants.size());

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG,"Here I keep track of the poll");
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d(TAG,"firebase child removed");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG,"firebase child moved");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG,"firebase cancel");
        }

    };

    private void updateUI(Restaurant restaurant) {
        placeName.setText(restaurant.getName());

        StringBuilder sb = new StringBuilder();
        sb.append("Rating: ").append(restaurant.getRating());
        sb.append("\n\n");
        sb.append("Close to: ").append(restaurant.getVicinity());

        placeSummary.setText(sb.toString());

        if (!restaurant.getPhotoUrl().equals("No Pic"))
            Picasso.with(getBaseContext()).load(restaurant.getPhotoUrl()).into(restaurantPic);
        hideProgressDialog();
        position++;
    }

    @OnClick(R.id.fab)
    public void FabClick(){
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
