package md.jcarcamo.pickaplace.utils;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by jcarcamo on 6/17/17.
 */

public class Poll {
    private String title;
    private boolean started = false;
    private boolean finished = false;
    private double latitude;
    private double longitude;
    private String timestamp;
    private List<FacebookUser> invited;

    public Poll() {

    }

    public Poll(String title, double latitude, double longitude, String timestamp, List<FacebookUser> invited) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.invited = invited;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<FacebookUser> getInvited() {
        return invited;
    }

    public void setInvited(List<FacebookUser> invited) {
        this.invited = invited;
    }
}
