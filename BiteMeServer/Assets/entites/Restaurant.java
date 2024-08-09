package entites;

//Class based on DB's Table


public class Restaurant {
    private int restaurantNumber;
    private String restaurantName;
    private int menuID;
    private String district;
    
    public Restaurant(int restaurantNumber, String restaurantName, int menuID, String district) {
        this.restaurantNumber = restaurantNumber;
        this.restaurantName = restaurantName;
        this.menuID = menuID;
        this.district = district;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    
    public int getMenuID() {
        return menuID;
    }
    
    public String getDistrict() {
        return district;
    }
    
}
