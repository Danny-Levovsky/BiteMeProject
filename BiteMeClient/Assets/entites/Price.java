package entites;

import java.io.Serializable;


/**
 * Represents a Price entity based on the corresponding database table.
 * This class implements Serializable for object serialization.
 */
public class Price implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
    private int dishID;
    private String size;
    private int price;


    /**
     * Constructs a new Price object with the specified details.
     *
     * @param dishID The unique identifier of the dish
     * @param size   The size of the dish
     * @param price  The price of the dish for the specified size
     */
    public Price(int dishID, String size, int price) {
        this.dishID = dishID;
        this.size = size;
        this.price = price;
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
     * Returns the size of the dish.
     *
     * @return The size of the dish
     */
    public String getSize() {
        return size;
    }

    
    /**
     * Returns the price of the dish.
     *
     * @return The price of the dish
     */
    public int getPrice() {
        return price;
    }
}

