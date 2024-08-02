package entites;

//Class based on DB's Table


public class Restaurant {
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
}
