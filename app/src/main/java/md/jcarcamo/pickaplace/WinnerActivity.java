package md.jcarcamo.pickaplace;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import md.jcarcamo.pickaplace.utils.Restaurant;

public class WinnerActivity extends BaseActivity {

    @BindView(R.id.placeNameTextViewW)
    TextView placeName;
    @BindView(R.id.imageViewW)
    ImageView restaurantPic;
    @BindView(R.id.placeSummaryTextViewW)
    TextView placeSummary;
    Restaurant winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        ButterKnife.bind(this);
        Intent prevIntent = getIntent();
        if(prevIntent != null){
            Parcelable par = prevIntent.getParcelableExtra("WINNER");
            winner = Parcels.unwrap(par);
            showProgressDialog();
            updateUI(winner);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(winner != null) {
                    /*String uri = String.format(Locale.ENGLISH, "geo:%f,%f", winner.getLatitude(), winner.getLongitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getApplicationContext().startActivity(intent);*/
                    try {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("geo:" +winner.getLatitude()
                                        + "," + winner.getLongitude()
                                        + "?q=" +winner.getLatitude()
                                        + "," + winner.getLongitude()
                                        + "(" + winner.getName() + ")"));
                        intent.setComponent(new ComponentName(
                                "com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity"));
                        getApplicationContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                        try {
                            getApplicationContext().startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.google.android.apps.maps")));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            getApplicationContext().startActivity(new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps")));
                        }

                        e.printStackTrace();
                    }
                }
            }
        });
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
}
