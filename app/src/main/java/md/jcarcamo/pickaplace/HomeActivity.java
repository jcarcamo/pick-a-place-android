package md.jcarcamo.pickaplace;

import android.net.Uri;
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
import md.jcarcamo.pickaplace.utils.ViewPagerAdapter;

public class HomeActivity extends BaseActivity implements
        CreatePollFragment.OnFragmentInteractionListener,
        FacebookFriendsFragment.OnListFragmentInteractionListener {
    private static final String TAG = "HomeActivity";

    private FirebaseAuth mAuth;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    CharSequence Titles[] = {"Pick a Place"};
    int Numboftabs = 1;



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
    public void onCreatePollInteraction(Uri uri) {
        Log.d(TAG,"Fragment Inception ");
    }

    @Override
    public void onFriendClick(FacebookUser friend) {
        Log.d(TAG,"Child Fragment Inception ");
        CreatePollFragment pollFragment = (CreatePollFragment)pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
        pollFragment.onFriendClick(friend);
    }

    public void onComplete(){
        Log.d(TAG,"Friends Loaded");
    }
}
