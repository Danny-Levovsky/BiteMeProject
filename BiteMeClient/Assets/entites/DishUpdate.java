package entites;

import java.io.Serializable;

/**
 * Represents a Dish entity based on the corresponding database table.
 * This class implements Serializable for object serialization.
 */
public class DishUpdate implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int dishID;
    private int RestaurantNumber;
    private int categoryId;
    private String dishName;

    
    /**
     * Constructs a new Dish with the specified details.
     *
     * @param dishID           The unique identifier for the dish
     * @param RestaurantNumber The restaurant number associated with this dish
     * @param categoryId       The category ID of the dish
     * @param dishName         The name of the dish
     */
    public DishUpdate(int dishID, int RestaurantNumber, int categoryId, String dishName) {
        this.dishID = dishID;
        this.RestaurantNumber = RestaurantNumber;
        this.categoryId = categoryId;
        this.dishName = dishName;
    }

    
    /**
     * Returns the dish ID.
     *
     * @return The dish ID
     */
    public int getDishID() {
        return dishID;
    }

    
    /**
     * Sets the dish ID.
     *
     * @param dishID The new dish ID
     */
    public void setDishID(int dishID) {
        this.dishID = dishID;
    }
    
    
    /**
     * Returns the restaurant number.
     *
     * @return The restaurant number
     */
    public int getRestaurantNumber() {
        return RestaurantNumber;
    }

    
    /**
     * Returns the category ID.
     *
     * @return The category ID
     */
    public int getCategoryId() {
        return categoryId;
    }

    
    /**
     * Returns the dish name.
     *
     * @return The dish name
     */
    public String getDishName() {
        return dishName;
    }
}
