package md.jcarcamo.pickaplace.utils;

import java.util.List;

/**
 * Created by jcarcamo on 6/18/17.
 */

public class RestaurantsList {
    private List<Restaurant> restaurants;

    public RestaurantsList(){

    }
    public RestaurantsList(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
