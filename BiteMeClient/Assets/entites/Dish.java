package entites;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dish {
    private final IntegerProperty dishID;
    private final StringProperty dishName;
    private final StringProperty categoryName;
    private final IntegerProperty dishPrice;
    private final ObjectProperty<ObservableList<String>> specifications;
    private final StringProperty selectedSpecification;

    public Dish(int dishID, String dishName, String categoryName, int dishPrice, ObservableList<String> specifications) {
        this.dishID = new SimpleIntegerProperty(dishID);
        this.dishName = new SimpleStringProperty(dishName);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.dishPrice = new SimpleIntegerProperty(dishPrice);
        this.specifications = new SimpleObjectProperty<>(specifications);
        this.selectedSpecification = new SimpleStringProperty();
    }

    // Getters
    public int getDishID() { return dishID.get(); }
    public String getDishName() { return dishName.get(); }
    public String getCategoryName() { return categoryName.get(); }
    public int getDishPrice() { return dishPrice.get(); }
    public ObservableList<String> getSpecifications() { return specifications.get(); }
    public String getSelectedSpecification() { return selectedSpecification.get(); }

    // Property getters
    public IntegerProperty dishIDProperty() { return dishID; }
    public StringProperty dishNameProperty() { return dishName; }
    public StringProperty categoryNameProperty() { return categoryName; }
    public IntegerProperty dishPriceProperty() { return dishPrice; }
    public ObjectProperty<ObservableList<String>> specificationsProperty() { return specifications; }
    public StringProperty selectedSpecificationProperty() { return selectedSpecification; }

    // Setters
    public void setDishID(int id) { dishID.set(id); }
    public void setDishName(String name) { dishName.set(name); }
    public void setCategoryName(String category) { categoryName.set(category); }
    public void setDishPrice(int price) { dishPrice.set(price); }
    public void setSpecifications(ObservableList<String> specs) { specifications.set(specs); }
    public void setSelectedSpecification(String spec) { selectedSpecification.set(spec); }
}