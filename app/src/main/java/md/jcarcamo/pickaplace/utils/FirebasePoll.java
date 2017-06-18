package md.jcarcamo.pickaplace.utils;

import android.hardware.camera2.params.Face;

import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by jcarcamo on 6/18/17.
 */

public class FirebasePoll {
    private List<FacebookUser> invited;
    private double latitude;
    private double longitude;
    private List<Restaurant> restaurants;
    private boolean started;
    private DateTime timestamp;

    public FirebasePoll(){

    }
    public FirebasePoll(List<FacebookUser> invited, double latitude, double longitude,
                        List<Restaurant> restaurants, boolean started, DateTime timestamp) {
        this.invited = invited;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurants = restaurants;
        this.started = started;
        this.timestamp = timestamp;
    }

    public List<FacebookUser> getInvited() {
        return invited;
    }

    public void setInvited(List<FacebookUser> invited) {
        this.invited = invited;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }
}
