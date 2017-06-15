package md.jcarcamo.pickaplace;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import md.jcarcamo.pickaplace.dummy.DummyContent;

public class FacebookFriendsActivity extends AppCompatActivity implements FacebookFriendsFragment.OnListFragmentInteractionListener {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_friends);
        mAuth = FirebaseAuth.getInstance();
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

    }
    @Override

    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        System.out.println("Interact!");
        Intent result = new Intent();
        //Parcelable parcel = Parcels.wrap(item);
        //result.putExtra("LLOOKUP", parcel);
        setResult(RESULT_OK, result);
        finish();
    }

}
