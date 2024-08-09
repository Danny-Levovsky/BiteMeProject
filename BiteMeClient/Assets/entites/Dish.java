package entites;

import java.io.Serializable;

//Class based on DB's Table

public class Dish implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int dishID;
    private int RestaurantNumber;
    private int categoryId;
    private String dishName;

    public Dish(int dishID, int RestaurantNumber, int categoryId, String dishName) {
        this.dishID = dishID;
        this.RestaurantNumber = RestaurantNumber;
        this.categoryId = categoryId;
        this.dishName = dishName;
    }

    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }
    
    public int getRestaurantNumber() {
        return RestaurantNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDishName() {
        return dishName;
    }
}
