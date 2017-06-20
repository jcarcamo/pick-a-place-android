package md.jcarcamo.pickaplace;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.jcarcamo.pickaplace.utils.FirebasePoll;
import md.jcarcamo.pickaplace.utils.Restaurant;

public class PollActivity extends BaseActivity  {

    private static final String TAG = "PollActivity";

    private List<Restaurant> restaurants;
    private String pollId;
    private DatabaseReference pollRef;
    private DatabaseReference voteRef;
    private DatabaseReference userRef;

    private long position;
    private Restaurant winner;
    private boolean isPollFinished = false;
    private boolean persistPosition = false;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.fab2)
    FloatingActionButton fab2;

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
        position = getIntent().getLongExtra("position",0);
        Log.d(TAG,"Poll Id: "+pollId);
        pollRef = FirebaseDatabase.getInstance().getReference("poll").child(pollId);
        pollRef.addValueEventListener(pollValueEventListener);

        voteRef = pollRef.child("restaurants");

        userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(AccessToken.getCurrentAccessToken().getUserId())
                .child("polls").child(pollId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("id")) {
                   persistPosition = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        showProgressDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    ValueEventListener pollValueEventListener = new ValueEventListener() {
        boolean firstTime = true;
        @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<FirebasePoll> t = new GenericTypeIndicator<FirebasePoll>() {};
                FirebasePoll fbp = dataSnapshot.getValue(t);
                if(fbp != null) {
                    if(!fbp.isFinished()) {
                        if (firstTime) {
                            restaurants = fbp.getRestaurants();
                            if (restaurants != null) {
                                Restaurant tmpRestaurant = restaurants.get((int) position);
                                updateUI(tmpRestaurant);
                                Log.d(TAG, "restaurants loaded: " + restaurants.size());
                                firstTime = false;
                            } else {
                                Toast.makeText(getApplicationContext(), "No places found. Wait a little bit or try another starting point!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        isPollFinished = true;
                        winner = fbp.getRestaurants().get(fbp.getWinner());
                        Parcelable winnerParcel = Parcels.wrap(winner);
                        Intent result = new Intent(PollActivity.this,WinnerActivity.class);
                        result.putExtra("WINNER", winnerParcel);
                        setResult(RESULT_OK, result);
                        finish();
                        //Toast.makeText(getApplicationContext(), "Finished poll!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Invite not found. Try again later!", Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                    finish();
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    @Override
    public void onResume(){
        super.onResume();
        pollRef = FirebaseDatabase.getInstance().getReference("poll").child(pollId);
        pollRef.addValueEventListener(pollValueEventListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(restaurants != null) {
            restaurants.clear();
        }
        pollRef.removeEventListener(pollValueEventListener);
    }

    private void updateUI(Restaurant restaurant) {
        placeName.setText(restaurant.getName());

        StringBuilder sb = new StringBuilder();
        sb.append("Rating: ").append(restaurant.getRating());
        sb.append("\n\n");
        sb.append("Close to: ").append(restaurant.getVicinity());

        placeSummary.setText(sb.toString());

        if (!restaurant.getPhotoUrl().equals("No Pic")) {
            Picasso.with(getBaseContext()).load(restaurant.getPhotoUrl()).into(restaurantPic);
        }else{
            String uri = "@drawable/pick_a_place_holder";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            restaurantPic.setImageDrawable(res);
        }
        hideProgressDialog();
    }

    @OnClick(R.id.fab)
    public void positiveClick(){
        if(position < restaurants.size()-1){
            showProgressDialog();
            Restaurant currentRestaurant = restaurants.get((int) position);
            if(!isPollFinished)
                voteRef.child(Integer.toString((int) position)).child("votes").setValue(currentRestaurant.getVotes()+1);
            position++;
            //if(position <= restaurants.size()-1) {
                Restaurant nextRestaurant = restaurants.get((int) position);
                updateUI(nextRestaurant);
            //}
        }else {
            Snackbar.make(fab, "Wating until poll is finished", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Action", null).show();
        }
    }

    @OnClick(R.id.fab2)
    public void negativeClick(){
        if(position < restaurants.size()-1){
            showProgressDialog();
            position++;
            Restaurant nextRestaurant = restaurants.get((int) position);
            updateUI(nextRestaurant);

        } else {
            Snackbar.make(fab2, "Wating until poll is finished", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onBackPressed(){
        if(persistPosition) {
            userRef.child("position").setValue(position);
        }
        super.onBackPressed();
        this.finish();
    }
}
