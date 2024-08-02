package entites;


//Class based on DB's Table

public class Price {
    private int priceID;
    private int dishID;
    private String size;
    private int price;

    
    public Price(int priceID, int dishID, String size, int price) {
        this.priceID = priceID;
        this.dishID = dishID;
        this.size = size;
        this.price = price;
    }
    
    
    public int getPriceID() {
        return priceID;
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

