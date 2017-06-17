package md.jcarcamo.pickaplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePollActivity extends BaseActivity {
    private static final String TAG = "CreatePollActivity";
    public static final int GET_LOCATION_REQUEST_CODE = 1;

    @BindView(R.id.location)
    TextView locationTextView;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.fab)
    public void FabClick(){
        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @OnClick(R.id.location)
    public void getStartingLocation(){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, GET_LOCATION_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case(GET_LOCATION_REQUEST_CODE):{
                switch (resultCode){
                    case(RESULT_OK):{
                        Place pl = PlaceAutocomplete.getPlace(this, data);
                        locationTextView.setText(pl.getName());
                        Log.i(TAG, "onActivityResult: " + pl.getName() + "/" + pl.getAddress());
                        break;
                    }
                    case(PlaceAutocomplete.RESULT_ERROR):{
                        Status stat = PlaceAutocomplete.getStatus(this, data);
                        Log.d(TAG, "onActivityResult: " + stat.getStatusMessage());
                        break;
                    }
                    case(RESULT_CANCELED):{
                        Log.i(TAG, "onActivityResult: Cancelled by the user");
                        break;
                    }
                    default:{
                        Log.i(TAG, "onActivityResult: Unknown resultCode");
                        break;
                    }
                }
                break;

            }
            default:{
                Log.i(TAG, "onActivityResult: Unknown Request Code");
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
