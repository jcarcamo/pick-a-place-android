package md.jcarcamo.pickaplace.utils;

import com.google.firebase.database.Exclude;

/**
 * Created by jcarcamo on 6/18/17.
 */

public class Restaurant {

    @Exclude
    private String id;

    private String name;
    private String photoUrl;
    private String rating;
    private String vicinity;

    public Restaurant(){

    }

    public Restaurant(String name, String photoUrl, String rating, String vicinity) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.rating = rating;
        this.vicinity = vicinity;
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
}
