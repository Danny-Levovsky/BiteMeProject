package entites;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int restaurantNumber;
    private String restaurantName;
    
    public Restaurant(int restaurantNumber, String restaurantName) {
        this.restaurantNumber = restaurantNumber;
        this.restaurantName = restaurantName;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    
    @Override
    public String toString() {
        return restaurantName;
    }
}