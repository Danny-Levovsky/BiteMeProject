package entites;

import java.io.Serializable;

/**
 * The RestaurantOrder class represents an order in a restaurant. It includes
 * details such as the order ID, customer number, order date and time, dish
 * name, quantity, delivery status, and order status. This class implements
 * Serializable to allow its objects to be serialized.
 * 
 * @author yosra
 */
public class RestaurantOrder implements Serializable {

	/**
	 * A unique identifier for the serialization runtime to associate with the
	 * RestaurantOrder class. This value ensures that a deserialized object is
	 * compatible with the current class definition.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The unique identifier for the order.
	 */
	private int orderId;

	/**
	 * The unique identifier for the customer who placed the order.
	 */
	private int customerNumber;

	/**
	 * The date and time when the order was placed.
	 */
	private String orderDateTime;

	/**
	 * The name of the dish ordered.
	 */
	private String dishName;

	/**
	 * The quantity of the dish ordered.
	 */
	private int quantity;

	/**
	 * Indicates whether the order is for delivery (1 if delivery, 0 otherwise).
	 */
	private int IsDelivery;

	/**
	 * The current status of the order.
	 */
	private String orderStatus;

	/**
	 * The size of the dish ordered.
	 */
	private String size;

	/**
	 * Additional specifications or instructions for the dish ordered.
	 */
	private String specification;

	/**
	 * Default constructor for creating a RestaurantOrder object.
	 */
	public RestaurantOrder() {
	};

	/**
	 * Constructor to initialize a RestaurantOrder object with all attributes.
	 * 
	 * @param orderID        the order ID
	 * @param customerNumber the customer number
	 * @param orderDateTime  the order date and time
	 * @param dishName       the name of the dish
	 * @param quantity       the quantity of the dish
	 * @param isDelivery     the delivery status (1 if delivery, 0 otherwise)
	 * @param orderStatus    the status of the order
	 */
	public RestaurantOrder(int orderID, int customerNumber, String orderDateTime, String dishName, int quantity,
			int IsDelivery, String orderStatus) {
		this.setOrderId(orderID);
		this.setCustomerNumber(customerNumber);
		this.setOrderDateTime(orderDateTime);
		this.setDishName(dishName);
		this.setQuantity(quantity);
		this.setIsDelivery(IsDelivery);
		this.setOrderStatus(orderStatus);
	}

	/**
	 * Gets the order ID.
	 * 
	 * @return the order ID
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * Sets the order ID.
	 * 
	 * @param orderId the order ID to set
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**
	 * Gets the customer number.
	 * 
	 * @return the customer number
	 */
	public int getCustomerNumber() {
		return customerNumber;
	}

	/**
	 * Sets the customer number.
	 * 
	 * @param customerNumber the customer number to set
	 */
	public void setCustomerNumber(int customerNumber) {
		this.customerNumber = customerNumber;
	}

	/**
	 * Gets the order date and time.
	 * 
	 * @return the order date and time
	 */
	public String getOrderDateTime() {
		return orderDateTime;
	}

	/**
	 * Sets the order date and time.
	 * 
	 * @param orderDateTime the order date and time to set
	 */
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	/**
	 * Gets the dish name.
	 * 
	 * @return the dish name
	 */
	public String getDishName() {
		return dishName;
	}

	/**
	 * Sets the dish name.
	 * 
	 * @param dishName the dish name to set
	 */
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	/**
	 * Gets the quantity of the dish.
	 * 
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity of the dish.
	 * 
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the delivery status.
	 * 
	 * @return the delivery status (1 if delivery, 0 otherwise)
	 */
	public int getIsDelivery() {
		return IsDelivery;
	}

	/**
	 * Sets the delivery status.
	 * 
	 * @param isDelivery the delivery status to set (1 if delivery, 0 otherwise)
	 */
	public void setIsDelivery(int isDelivery) {
		IsDelivery = isDelivery;
	}

	/**
	 * Gets the order status.
	 * 
	 * @return the order status
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the order status.
	 * 
	 * @param orderStatus the order status to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * Gets the size of the dish.
	 * 
	 * @return the size of the dish
	 */
	public String getSize() {
		return size;
	}

	/**
	 * Sets the size of the dish.
	 * 
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * Gets the additional specifications or instructions for the dish ordered.
	 * 
	 * @return the specifications
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * Sets the additional specifications or instructions for the dish ordered.
	 * 
	 * @param specification the specifications to set
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}
}