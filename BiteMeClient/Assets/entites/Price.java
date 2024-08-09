package entites;

import java.io.Serializable;

//Class based on DB's Table

public class Price implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
    private int dishID;
    private String size;
    private int price;

    
    public Price(int dishID, String size, int price) {
        this.dishID = dishID;
        this.size = size;
        this.price = price;
    }

    public int getDishID() {
        return dishID;
    }

    public String getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }
}

