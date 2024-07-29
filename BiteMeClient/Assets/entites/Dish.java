package tempForMini;


//Class based on DB's Table

public class Dish {
    private int dishID;
    private int restaurantNumber;
    private int categoryId;
    private String dishName;
    
    public Dish(int dishID, int restaurantNumber, int categoryId, String dishName) {
        this.dishID = dishID;
        this.restaurantNumber = restaurantNumber;
        this.categoryId = categoryId;
        this.dishName = dishName;
    }

    public int getDishID() {
        return dishID;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDishName() {
        return dishName;
    }
}
