package entites;

import java.io.Serializable;
import java.util.Objects;

import javafx.beans.property.*;

public class OrderItem {
    private final StringProperty dishID;
    private final StringProperty dishName;
    private final StringProperty categoryName;
    private final IntegerProperty dishPrice;
    private final StringProperty selectedSpecification;
    private final IntegerProperty quantity;

    public OrderItem(Dish dish, String selectedSpecification, int quantity) {
        this.dishID = new SimpleStringProperty(dish.getDishID());
        this.dishName = new SimpleStringProperty(dish.getDishName());
        this.categoryName = new SimpleStringProperty(dish.getCategoryName());
        this.dishPrice = new SimpleIntegerProperty(dish.getDishPrice());
        this.selectedSpecification = new SimpleStringProperty(selectedSpecification);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getters
    public String getDishID() { return dishID.get(); }
    public String getDishName() { return dishName.get(); }
    public String getCategoryName() { return categoryName.get(); }
    public int getDishPrice() { return dishPrice.get(); }
    public String getSelectedSpecification() { return selectedSpecification.get(); }
    public int getQuantity() { return quantity.get(); }

    // Property getters
    public StringProperty dishIDProperty() { return dishID; }
    public StringProperty dishNameProperty() { return dishName; }
    public StringProperty categoryNameProperty() { return categoryName; }
    public IntegerProperty dishPriceProperty() { return dishPrice; }
    public StringProperty selectedSpecificationProperty() { return selectedSpecification; }
    public IntegerProperty quantityProperty() { return quantity; }

    // Setters
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return getDishID().equals(orderItem.getDishID()) &&
               getSelectedSpecification().equals(orderItem.getSelectedSpecification());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDishID(), getSelectedSpecification());
    }
}