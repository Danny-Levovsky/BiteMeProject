package entites;

import java.io.Serializable;

//Class based on DB's Table
public class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;
    private int categoryID;
    private String name;

    
    public Category(int categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
    }
    
    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }
}
