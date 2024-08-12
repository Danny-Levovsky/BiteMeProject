package entites;

import java.io.Serializable;


/**
 * Represents a Category entity based on the corresponding database table.
 * This class implements Serializable for object serialization.
 */
public class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private int categoryID;
    private String name;

    
    /**
     * Constructs a new Category object with the specified details.
     *
     * @param categoryID The unique identifier for the category
     * @param name       The name of the category
     */
    public Category(int categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
    }
    
    
    /**
     * Returns the category ID.
     *
     * @return The category ID
     */
    public int getCategoryID() {
        return categoryID;
    }

    
    /**
     * Returns the name of the category.
     *
     * @return The name of the category
     */
    public String getName() {
        return name;
    }
}
