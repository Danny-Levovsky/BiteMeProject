package entites;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Serializable {

    private transient IntegerProperty dishID;
    private transient StringProperty dishName;
    private transient StringProperty categoryName;
    private transient IntegerProperty dishPrice;
    private transient ObjectProperty<ObservableList<String>> specifications;
    private transient StringProperty selectedSpecification;

    public Dish(int dishID, String dishName, String categoryName, int dishPrice, ObservableList<String> specifications) {
        this.dishID = new SimpleIntegerProperty(dishID);
        this.dishName = new SimpleStringProperty(dishName);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.dishPrice = new SimpleIntegerProperty(dishPrice);
        this.specifications = new SimpleObjectProperty<>(specifications);
        this.selectedSpecification = new SimpleStringProperty("None");
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
    
 // You might need to add custom serialization methods if the JavaFX properties
    // don't serialize well. For example:

    private static final long serialVersionUID = 1L;
    
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(getDishID());
        out.writeUTF(getDishName());
        out.writeUTF(getCategoryName());
        out.writeInt(getDishPrice());
        out.writeObject(new ArrayList<>(getSpecifications()));
        out.writeUTF(getSelectedSpecification());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        dishID = new SimpleIntegerProperty(in.readInt());
        dishName = new SimpleStringProperty(in.readUTF());
        categoryName = new SimpleStringProperty(in.readUTF());
        dishPrice = new SimpleIntegerProperty(in.readInt());
        specifications = new SimpleObjectProperty<>(FXCollections.observableArrayList((List<String>) in.readObject()));
        selectedSpecification = new SimpleStringProperty(in.readUTF());
    }

    
}