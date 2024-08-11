package entites;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dish {
	private final StringProperty dishID;
    private final StringProperty dishName;
    private final StringProperty categoryName;
    private final IntegerProperty dishPrice;
    private final ObjectProperty<ObservableList<String>> specifications;
    private final StringProperty selectedSpecification;
    private final IntegerProperty quantity;
    private final ObjectProperty<Map<String, Integer>> sizePrices;
    

    public Dish(String dishID, String dishName, String categoryName, int dishPrice, ObservableList<String> specifications) {
        this.dishID = new SimpleStringProperty(dishID);
        this.dishName = new SimpleStringProperty(dishName);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.dishPrice = new SimpleIntegerProperty(dishPrice);
        this.specifications = new SimpleObjectProperty<>(specifications);
        this.selectedSpecification = new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty(0);
        this.sizePrices = new SimpleObjectProperty<>(new HashMap<>());
    }
    
 // Copy constructor
    public Dish(Dish other, String selectedSpecification) {
        this.dishID = new SimpleStringProperty(other.getDishID());
        this.dishName = new SimpleStringProperty(other.getDishName());
        this.categoryName = new SimpleStringProperty(other.getCategoryName());
        this.dishPrice = new SimpleIntegerProperty(other.getDishPrice());
        this.specifications = new SimpleObjectProperty<>(FXCollections.observableArrayList(other.getSpecifications()));
        this.selectedSpecification = new SimpleStringProperty(selectedSpecification);
        this.quantity = new SimpleIntegerProperty(0);
        this.sizePrices = new SimpleObjectProperty<>(new HashMap<>(other.getSizePrices()));
    }

    // Getters
    public String getDishID() { return dishID.get(); }
    public String getDishName() { return dishName.get(); }
    public String getCategoryName() { return categoryName.get(); }
    public int getDishPrice() { return dishPrice.get(); }
    public ObservableList<String> getSpecifications() { return specifications.get(); }
    public String getSelectedSpecification() { return selectedSpecification.get(); }
    
    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    // Property getters
    public StringProperty dishIDProperty() { return dishID; }
    public StringProperty dishNameProperty() { return dishName; }
    public StringProperty categoryNameProperty() { return categoryName; }
    public IntegerProperty dishPriceProperty() { return dishPrice; }
    public ObjectProperty<ObservableList<String>> specificationsProperty() { return specifications; }
    public StringProperty selectedSpecificationProperty() { return selectedSpecification; }
    
    public Map<String, Integer> getSizePrices() {
        return sizePrices.get();
    }

    public void setSizePrices(Map<String, Integer> prices) {
        this.sizePrices.set(prices);
    }

    public ObjectProperty<Map<String, Integer>> sizePricesProperty() {
        return sizePrices;
    }

    public int getPriceForSize(String size) {
        return sizePrices.get().getOrDefault(size, getDishPrice());
    }

    public void addSizePrice(String size, int price) {
        Map<String, Integer> prices = sizePrices.get();
        prices.put(size, price);
        sizePrices.set(prices);
    }

    // Setters
    public void setDishID(String id) { dishID.set(id); }
    public void setDishName(String name) { dishName.set(name); }
    public void setCategoryName(String category) { categoryName.set(category); }
    public void setDishPrice(int price) { dishPrice.set(price); }
    public void setSpecifications(ObservableList<String> specs) { specifications.set(specs); }
    public void setSelectedSpecification(String spec) { selectedSpecification.set(spec); }
}