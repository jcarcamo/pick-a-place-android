package md.jcarcamo.pickaplace.utils;

import com.google.firebase.database.Exclude;

import org.parceler.Parcel;

/**
 * Created by jcarcamo on 6/18/17.
 */

@Parcel
public class Restaurant {

    @Exclude
    private String id;

    private String name;
    private String photoUrl;
    private String rating;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private int votes;

    public Restaurant(){

    }

    public Restaurant(String id, String name, String photoUrl, String rating, String vicinity, Double latitude, Double longitude, int votes) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.vicinity = vicinity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.votes = votes;
    }

    public int getVotes() {

        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
