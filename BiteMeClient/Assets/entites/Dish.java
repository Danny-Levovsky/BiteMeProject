package entites;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Dish class represents a dish in a restaurant menu. It includes details
 * such as dish ID, name, category, price, specifications, selected
 * specification, quantity, and size-based pricing. This class makes use of
 * JavaFX properties to support data binding in a JavaFX application.
 * 
 * @author yosra
 */
public class Dish {

	/**
	 * The ID of the dish.
	 */
	private final StringProperty dishID;

	/**
	 * The name of the dish.
	 */
	private final StringProperty dishName;

	/**
	 * The category name to which the dish belongs.
	 */
	private final StringProperty categoryName;

	/**
	 * The price of the dish.
	 */
	private final IntegerProperty dishPrice;

	/**
	 * A list of specifications for the dish (e.g., "large", "no coriander").
	 */
	private final ObjectProperty<ObservableList<String>> specifications;

	/**
	 * The selected specification for the dish.
	 */
	private final StringProperty selectedSpecification;

	/**
	 * The quantity of the dish being ordered.
	 */
	private final IntegerProperty quantity;

	/**
	 * A map of size-based prices for the dish.
	 */
	private final ObjectProperty<Map<String, Integer>> sizePrices;

	/**
	 * Constructs a new Dish object with the specified details.
	 * 
	 * @param dishID         The ID of the dish.
	 * @param dishName       The name of the dish.
	 * @param categoryName   The category to which the dish belongs.
	 * @param dishPrice      The price of the dish.
	 * @param specifications A list of specifications for the dish.
	 */
	public Dish(String dishID, String dishName, String categoryName, int dishPrice,
			ObservableList<String> specifications) {
		this.dishID = new SimpleStringProperty(dishID);
		this.dishName = new SimpleStringProperty(dishName);
		this.categoryName = new SimpleStringProperty(categoryName);
		this.dishPrice = new SimpleIntegerProperty(dishPrice);
		this.specifications = new SimpleObjectProperty<>(specifications);
		this.selectedSpecification = new SimpleStringProperty();
		this.quantity = new SimpleIntegerProperty(0);
		this.sizePrices = new SimpleObjectProperty<>(new HashMap<>());
	}

	/**
	 * Copy constructor for creating a new Dish object by copying another Dish
	 * object.
	 * 
	 * @param other                 The Dish object to copy.
	 * @param selectedSpecification The selected specification for the new Dish
	 *                              object.
	 */
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

	/**
	 * Gets the dish ID.
	 * 
	 * @return The dish ID.
	 */
	public String getDishID() {
		return dishID.get();
	}

	/**
	 * Gets the dish name.
	 * 
	 * @return The dish name.
	 */
	public String getDishName() {
		return dishName.get();
	}

	/**
	 * Gets the category name to which the dish belongs.
	 * 
	 * @return The category name.
	 */
	public String getCategoryName() {
		return categoryName.get();
	}

	/**
	 * Gets the price of the dish.
	 * 
	 * @return The dish price.
	 */
	public int getDishPrice() {
		return dishPrice.get();
	}

	/**
	 * Gets the list of specifications for the dish.
	 * 
	 * @return The list of specifications.
	 */
	public ObservableList<String> getSpecifications() {
		return specifications.get();
	}

	/**
	 * Gets the selected specification for the dish.
	 * 
	 * @return The selected specification.
	 */
	public String getSelectedSpecification() {
		return selectedSpecification.get();
	}

	/**
	 * Gets the quantity of the dish being ordered.
	 * 
	 * @return The quantity.
	 */
	public int getQuantity() {
		return quantity.get();
	}

	/**
	 * Gets the property representing the quantity of the dish.
	 * 
	 * @return The quantity property.
	 */
	public IntegerProperty quantityProperty() {
		return quantity;
	}

	/**
	 * Sets the quantity of the dish being ordered.
	 * 
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	/**
	 * Gets the property representing the dish ID.
	 * 
	 * @return The dish ID property.
	 */
	public StringProperty dishIDProperty() {
		return dishID;
	}

	/**
	 * Gets the property representing the dish name.
	 * 
	 * @return The dish name property.
	 */
	public StringProperty dishNameProperty() {
		return dishName;
	}

	/**
	 * Gets the property representing the category name.
	 * 
	 * @return The category name property.
	 */
	public StringProperty categoryNameProperty() {
		return categoryName;
	}

	/**
	 * Gets the property representing the dish price.
	 * 
	 * @return The dish price property.
	 */
	public IntegerProperty dishPriceProperty() {
		return dishPrice;
	}

	/**
	 * Gets the property representing the list of specifications.
	 * 
	 * @return The specifications property.
	 */
	public ObjectProperty<ObservableList<String>> specificationsProperty() {
		return specifications;
	}

	/**
	 * Gets the property representing the selected specification.
	 * 
	 * @return The selected specification property.
	 */
	public StringProperty selectedSpecificationProperty() {
		return selectedSpecification;
	}

	/**
	 * Gets the map of size prices for this dish.
	 * 
	 * @return A Map where the key is the size and the value is the price.
	 */
	public Map<String, Integer> getSizePrices() {
		return sizePrices.get();
	}

	/**
	 * Sets the map of size prices for this dish.
	 * 
	 * @param prices A Map where the key is the size and the value is the price.
	 */
	public void setSizePrices(Map<String, Integer> prices) {
		this.sizePrices.set(prices);
	}

	/**
	 * Gets the property containing the map of size prices.
	 * 
	 * @return The ObjectProperty containing the size prices map.
	 */
	public ObjectProperty<Map<String, Integer>> sizePricesProperty() {
		return sizePrices;
	}

	/**
	 * Gets the price for a specific size of this dish.
	 * 
	 * @param size The size to get the price for.
	 * @return The price for the given size, or the default dish price if the size
	 *         is not found.
	 */
	public int getPriceForSize(String size) {
		return sizePrices.get().getOrDefault(size, getDishPrice());
	}

	/**
	 * Adds or updates a price for a specific size of this dish.
	 * 
	 * @param size  The size to set the price for.
	 * @param price The price for the given size.
	 */
	public void addSizePrice(String size, int price) {
		Map<String, Integer> prices = sizePrices.get();
		prices.put(size, price);
		sizePrices.set(prices);
	}

	/**
	 * Sets the dish ID.
	 * 
	 * @param id The dish ID to set.
	 */
	public void setDishID(String id) {
		dishID.set(id);
	}

	/**
	 * Sets the dish name.
	 * 
	 * @param name The dish name to set.
	 */
	public void setDishName(String name) {
		dishName.set(name);
	}

	/**
	 * Sets the category name.
	 * 
	 * @param category The category name to set.
	 */
	public void setCategoryName(String category) {
		categoryName.set(category);
	}

	/**
	 * Sets the dish price.
	 * 
	 * @param price The dish price to set.
	 */
	public void setDishPrice(int price) {
		dishPrice.set(price);
	}

	/**
	 * Sets the list of specifications.
	 * 
	 * @param specs The list of specifications to set.
	 */
	public void setSpecifications(ObservableList<String> specs) {
		specifications.set(specs);
	}

	/**
	 * Sets the selected specification.
	 * 
	 * @param spec The selected specification to set.
	 */
	public void setSelectedSpecification(String spec) {
		selectedSpecification.set(spec);
	}
}