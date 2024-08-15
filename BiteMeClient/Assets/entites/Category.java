package entites;

import java.io.Serializable;


/**
 * Represents a Category entity based on the corresponding database table.
 * This class implements {@link Serializable} to allow category objects to be serialized.
 * 
 * @author yosra
 */
public class Category implements Serializable {
	
	/**
	 * A unique identifier for this {@code Serializable} class.
	 * This ID is used to ensure that the class can be correctly deserialized.
	 */
	private static final long serialVersionUID = 1L;
	
	 /**
     * The unique identifier for the category.
     */
    private int categoryID;
    
    /**
     * The name of the category.
     */
    private String name;
    
    /**
     * Constructs a new {@code Category} object with the specified details.
     *
     * @param categoryID The unique identifier for the category.
     * @param name       The name of the category.
     */
    public Category(int categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
    }
     
    /**
     * Returns the category ID.
     *
     * @return The category ID.
     */
    public int getCategoryID() {
        return categoryID;
    }
   
    /**
     * Returns the name of the category.
     *
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }
}
