package md.jcarcamo.pickaplace.utils;

import android.hardware.camera2.params.Face;

import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by jcarcamo on 6/18/17.
 */

public class FirebasePoll {
    private String title;
    private List<FacebookUser> invited;
    private double latitude;
    private double longitude;
    private List<Restaurant> restaurants;
    private boolean started;
    private boolean finished;
    private String timestamp;
    private int winner;

    public FirebasePoll(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
