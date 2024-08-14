package entites;

import java.util.Objects;

import javafx.beans.property.*;

/**
 * The OrderItem class represents an item in an order, including details such as
 * the dish ID, dish name, category name, dish price, selected specification,
 * and quantity. This class uses JavaFX properties for easy binding and
 * updating.
 * 
 * @author yosra
 */
public class OrderItem {

	/**
	 * The ID of the dish.
	 */
	private final StringProperty dishID;

	/**
	 * The name of the dish.
	 */
	private final StringProperty dishName;

	/**
	 * The name of the category to which the dish belongs.
	 */
	private final StringProperty categoryName;

	/**
	 * The price of the dish.
	 */
	private final IntegerProperty dishPrice;

	/**
	 * The specification selected for the dish (e.g., size, customization).
	 */
	private final StringProperty selectedSpecification;

	/**
	 * The quantity of the dish in the order.
	 */
	private final IntegerProperty quantity;

	/**
	 * Constructs a new OrderItem with the specified dish, selected specification,
	 * and quantity.
	 * 
	 * @param dish                  The dish associated with this order item
	 * @param selectedSpecification The selected specification for the dish
	 * @param quantity              The quantity of the dish in the order
	 */
	public OrderItem(Dish dish, String selectedSpecification, int quantity) {
		this.dishID = new SimpleStringProperty(dish.getDishID());
		this.dishName = new SimpleStringProperty(dish.getDishName());
		this.categoryName = new SimpleStringProperty(dish.getCategoryName());
		this.dishPrice = new SimpleIntegerProperty(dish.getDishPrice());
		this.selectedSpecification = new SimpleStringProperty(selectedSpecification);
		this.quantity = new SimpleIntegerProperty(quantity);
	}

	/**
	 * Gets the dish ID.
	 * 
	 * @return The dish ID
	 */
	public String getDishID() {
		return dishID.get();
	}

	/**
	 * Gets the dish name.
	 * 
	 * @return The dish name
	 */
	public String getDishName() {
		return dishName.get();
	}

	/**
	 * Gets the category name of the dish.
	 * 
	 * @return The category name
	 */
	public String getCategoryName() {
		return categoryName.get();
	}

	/**
	 * Gets the dish price.
	 * 
	 * @return The dish price
	 */
	public int getDishPrice() {
		return dishPrice.get();
	}

	/**
	 * Gets the selected specification for the dish.
	 * 
	 * @return The selected specification
	 */
	public String getSelectedSpecification() {
		return selectedSpecification.get();
	}

	/**
	 * Gets the quantity of the dish in the order.
	 * 
	 * @return The quantity
	 */
	public int getQuantity() {
		return quantity.get();
	}

	/**
	 * Gets the property for the dish ID.
	 * 
	 * @return The dish ID property
	 */
	public StringProperty dishIDProperty() {
		return dishID;
	}

	/**
	 * Gets the property for the dish name.
	 * 
	 * @return The dish name property
	 */
	public StringProperty dishNameProperty() {
		return dishName;
	}

	/**
	 * Gets the property for the category name.
	 * 
	 * @return The category name property
	 */
	public StringProperty categoryNameProperty() {
		return categoryName;
	}

	/**
	 * Gets the property for the dish price.
	 * 
	 * @return The dish price property
	 */
	public IntegerProperty dishPriceProperty() {
		return dishPrice;
	}

	/**
	 * Gets the property for the selected specification.
	 * 
	 * @return The selected specification property
	 */
	public StringProperty selectedSpecificationProperty() {
		return selectedSpecification;
	}

	/**
	 * Gets the property for the quantity of the dish in the order.
	 * 
	 * @return The quantity property
	 */
	public IntegerProperty quantityProperty() {
		return quantity;
	}

	/**
	 * Sets the quantity of the dish in the order.
	 * 
	 * @param quantity The quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	/**
	 * Checks if this OrderItem is equal to another object. Two OrderItems are
	 * considered equal if they have the same dish ID and selected specification.
	 * 
	 * @param o The object to compare with
	 * @return true if the objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderItem orderItem = (OrderItem) o;
		return getDishID().equals(orderItem.getDishID())
				&& getSelectedSpecification().equals(orderItem.getSelectedSpecification());
	}

	/**
	 * Returns a hash code value for the OrderItem based on its dish ID and selected
	 * specification.
	 * 
	 * @return The hash code value
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getDishID(), getSelectedSpecification());
	}
}