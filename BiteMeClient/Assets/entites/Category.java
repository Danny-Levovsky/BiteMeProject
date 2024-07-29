package entites;

//Class based on DB's Table
public class Category {
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
