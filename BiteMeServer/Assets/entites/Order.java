package entites;

import java.io.Serializable;


/**
 * The Order class represents a customer's order at a restaurant. 
 * It includes various details such as the order number, customer number, restaurant name, 
 * order status, delivery details, and more. This class implements Serializable 
 * to allow its objects to be serialized.
 * 
 * @author yosra
 */
public class Order implements Serializable {
	
	/**
     * A unique identifier for the serialization runtime to associate with the Order class.
     * This value ensures that a deserialized object is compatible with the current class definition.
     */
	private static final long serialVersionUID = 1L;
	
	/**
     * The unique identifier for the order.
     */
	private int orderNumber;

	/**
     * The unique identifier for the customer who placed the order.
     */
	private int customerNumber;
	
	 /**
     * The name of the restaurant where the order was placed.
     */
    private String restaurantName;
    
    /**
     * The current status of the order.
     */
    private OrderStatus orderStatus;
    
    /**
     * Indicates whether the order was delivered on time.
     */
    private DelieveredOnTime delieveredOnTime;
    
    /**
     * The total price of the order.
     */
    private int orderPrice;
  
    /**
    * The date and time when the order was placed.
    */
    private String orderDateTime;  
    
    /**
     * The requested date and time for the order.
     */
    private String requestedDateTime;
    
    /**
     * Indicates whether the order was placed early (1 for early, 0 otherwise).
     */
    private int isEarlyOrder;
       
    /**
     * Enum representing the status of the order.
     */
    public enum OrderStatus{
    	PENDING, RECEIVED, APPROVED, READY, DELIEVERD
    }
    
    /**
     * Enum representing whether the order was delivered on time.
     */
    public enum DelieveredOnTime{
    	YES, NO, NULL;
    }
    
    /**
     * Constructs an Order object with the specified details.
     * 
     * @param orderNumber     The unique identifier for the order.
     * @param restaurantName  The name of the restaurant where the order was placed.
     * @param restaurantNumber The unique identifier for the restaurant (not used, placeholder).
     * @param customerNumber  The unique identifier for the customer who placed the order.
     */
    public Order(int orderNumber, String restaurantName, int restaurantNumber, int customerNumber) {
    	this.orderNumber = orderNumber;
    	this.restaurantName = restaurantName;
    	this.customerNumber = customerNumber;
        this.orderStatus = OrderStatus.PENDING;
        this.delieveredOnTime = DelieveredOnTime.NULL;
        this.orderPrice = 0;
    }
    
    /**
     * Constructs an Order object with the specified order number and order date/time.
     * 
     * @param orderNumber    The unique identifier for the order.
     * @param orderDateTime  The date and time when the order was placed.
     */
    public Order(int orderNumber, String orderDateTime) {
        this.orderNumber = orderNumber;
        this.orderDateTime = orderDateTime;
    }
    
    /**
     * Returns the unique identifier for the order.
     * 
     * @return The order number.
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Returns the date and time when the order was placed.
     * 
     * @return The order date and time.
     */
    public String getOrderDateTime() {
        return orderDateTime;
    }
    
    /**
     * Returns the requested date and time for the order.
     * 
     * @return The requested date and time.
     */
    public String getRequestedDateTime() {
        return requestedDateTime;
    }
    
    /**
     * Returns whether the order was placed early.
     * 
     * @return 1 if the order was placed early, 0 otherwise.
     */
    public int getIsEarlyOrder() {
        return isEarlyOrder;
    }
    
    /**
     * Returns the unique identifier for the restaurant where the order was placed.
     * 
     * @return The restaurant number.
     */
    public int getRestaurantNumber() {
        return orderNumber;
    }
    
    /**
     * Returns the name of the restaurant where the order was placed.
     * 
     * @return The restaurant name.
     */
    public String getRestaurantName() {
        return restaurantName;
    }
    
    /**
     * Returns the unique identifier for the customer who placed the order.
     * 
     * @return The customer number.
     */
    public int getCustomerNumber() {
    	return customerNumber;
    }
    
    /**
     * Returns the total price of the order.
     * 
     * @return The order price.
     */
    public int getOrderPrice() {
    	return orderPrice;
    }
    
    /**
     * Sets the total price of the order.
     * 
     * @param orderPrice The total price to set.
     */
    public void setOrderPrice(int orderPrice) {
    	this.orderPrice = orderPrice;
    }
    
    /**
     * Returns the current status of the order.
     * 
     * @return The order status.
     */
    public OrderStatus getOrderStatus() {
    	return orderStatus;
    }
    
    /**
     * Sets the status of the order.
     * 
     * @param orderStatus The status to set for the order.
     */
    public void setOrderStatus(OrderStatus orderStatus) {
    	this.orderStatus = orderStatus;
    }
    
    /**
     * Returns whether the order was delivered on time.
     * 
     * @return The delivery time status of the order.
     */
    public DelieveredOnTime getDeliveryTime() {
    	return delieveredOnTime;
    }
    
    /**
     * Sets whether the order was delivered on time.
     * 
     * @param delieveredOnTime The delivery time status to set.
     */
    public void setDeliveryTime(DelieveredOnTime delieveredOnTime) {
    	this.delieveredOnTime = delieveredOnTime;
    }
    

}