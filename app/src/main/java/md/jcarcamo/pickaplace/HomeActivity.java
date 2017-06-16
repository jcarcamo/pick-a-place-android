package md.jcarcamo.pickaplace;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import md.jcarcamo.pickaplace.dummy.DummyContent;
import md.jcarcamo.pickaplace.utils.FacebookUser;
import md.jcarcamo.pickaplace.utils.FriendsGraphResponse;
import md.jcarcamo.pickaplace.utils.ViewPagerAdapter;

public class HomeActivity extends BaseActivity implements FacebookFriendsFragment.OnListFragmentInteractionListener {
    private static final String TAG = "HomeActivity";

    public static List<FacebookUser> friends;
    public List<FacebookUser> selectedFriends;

    private FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    CharSequence Titles[] = {"Pick a Place"};
    int Numboftabs = 1;

    @BindView(R.id.selectedFriendsTextView) TextView selectedFriendsTv;

    protected void getFriends(){
        showProgressDialog();
        AccessToken token = AccessToken.getCurrentAccessToken();
        /* make the API call */
        GraphRequest gr = new GraphRequest(
                token,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                            /* handle the result */
                        Log.d(TAG, "friends:success "+response.getRawResponse());
                        try {
                            ObjectMapper om = new ObjectMapper();
                            FriendsGraphResponse friendsResponse =
                                    om.readValue(response
                                            .getRawResponse(), FriendsGraphResponse.class
                                    );
                            friends = friendsResponse.getData();
                            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                            pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

                            // Assigning ViewPager View and setting the adapter
                            viewPager = (ViewPager) findViewById(R.id.viewPager);
                            viewPager.setAdapter(pagerAdapter);

                            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                            tabLayout.setupWithViewPager(viewPager);

                            for (FacebookUser fbUser : friendsResponse.getData()){
                                Log.d(TAG, "jsonparse:success:name "+fbUser.getName());
                                Log.d(TAG, "jsonparse:success:id "+fbUser.getId());
                            }


                        }catch (IOException ioe){
                            Log.d(TAG, "jsonparse:IO Exception"+ioe.getMessage());
                        }
                        hideProgressDialog();

                    }
                }
        );
        gr.executeAsync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        this.getFriends();
        selectedFriends = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getFriends();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signoutItem:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut() {
        if(mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            LoginManager.getInstance().logOut();
        }
        finish();

    }
    @Override

    public void onListFragmentInteraction(FacebookUser friend) {
        Log.d(TAG,"friend selected " + friend.getId());
        boolean isFriendAlreadySelected = selectedFriends.contains(friend);
        if(!isFriendAlreadySelected){
            selectedFriends.add(friend);
            selectedFriendsTv.setText(selectedFriendsTv.getText() + ", " + friend.getName());
        }else{
            selectedFriends.remove(friend);
            StringBuilder sb = new StringBuilder();
            for(FacebookUser currentFriend : selectedFriends ){
                sb.append(currentFriend.getName()).append(",");
            }
            selectedFriendsTv.setText(sb.toString());
        }

        //Intent result = new Intent();
        //Parcelable parcel = Parcels.wrap(item);
        //result.putExtra("LLOOKUP", parcel);
        //setResult(RESULT_OK, result);
        //finish();
    }

}
