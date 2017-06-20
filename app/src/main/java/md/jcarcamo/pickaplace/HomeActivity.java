package md.jcarcamo.pickaplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import md.jcarcamo.pickaplace.utils.FacebookUser;
import md.jcarcamo.pickaplace.utils.PollInvites;
import md.jcarcamo.pickaplace.utils.ViewPagerAdapter;

public class HomeActivity extends BaseActivity implements
        CreatePollFragment.OnFragmentInteractionListener,
        FacebookFriendsFragment.OnListFragmentInteractionListener,
        PollInvitesFragment.OnListFragmentInteractionListener{
    private static final String TAG = "HomeActivity";
    public static final int START_POLL_REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    CharSequence Titles[] = {"Pick a Place", "My Invites"};
    int Numboftabs = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        // Assigning ViewPager View and setting the adapter
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onCreatePollInteraction() {
        Log.d(TAG,"Fragment Inception ");
        FacebookFriendsFragment friendsFragment = (FacebookFriendsFragment) pagerAdapter
                .getRegisteredFragment(viewPager.getCurrentItem()).getChildFragmentManager()
                .findFragmentById(R.id.fragment);
        friendsFragment.notifyDataSetChanged(CreatePollFragment.friends);
    }

    @Override
    public void onFriendClick(FacebookUser friend) {
        Log.d(TAG,"Child Fragment Inception ");
        CreatePollFragment pollFragment = (CreatePollFragment)pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        pollFragment.onFriendClick(friend);
    }

    @Override
    public void openPoll(PollInvites item) {
        Log.d(TAG,"Invites handler");
        Intent pollIntent = new Intent(this, PollActivity.class);
        pollIntent.putExtra("pollId",item.getId());
        pollIntent.putExtra("position", item.getPosition());
        startActivityForResult(pollIntent,START_POLL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (START_POLL_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.hasExtra("WINNER")) {
                        Parcelable par = data.getParcelableExtra("WINNER");
                        Intent result = new Intent(this, WinnerActivity.class);
                        result.putExtra("WINNER", par);
                        startActivity(result);
                    }
                }
                break;
            }
        }
    }
}
