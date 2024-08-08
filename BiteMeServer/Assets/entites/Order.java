package entites;

import java.io.Serializable;

public class Order implements Serializable {
	
	//private static final long serialVersionUID = 1L;
	
	private int orderNumber;
	private int customerNumber;
    private String restaurantName;
    private OrderStatus orderStatus;
    private DelieveredOnTime delieveredOnTime;
    private int orderPrice;
    private DeliveryDetail deliveryDetail; 
    
    private String orderDateTime;  //used to approve receiving order 
    private String requestedDateTime;
    private int isEarlyOrder;
    
    
    
    
    //System creates Order while Customer is in the process of ordering
    //Initial Order status is set to PENDING until it is sent to the restaurant
    //For an employee to approve receiving it.
    public enum OrderStatus{
    	PENDING, RECEIVED, APPROVED, READY, DELIEVERD
    }
    
    public enum DelieveredOnTime{
    	YES, NO, NULL;
    }
    
    
    //Order Constructor. Initial order instance DelieveredOnTime
    //set to NULL. Maybe set initial OrderStatus to RECEIVED
    //Since you create the instance when you receive the order?
    public Order(int orderNumber, String restaurantName, int restaurantNumber, int customerNumber) {
    	this.orderNumber = orderNumber;
    	this.restaurantName = restaurantName;
    	this.customerNumber = customerNumber;
        this.orderStatus = OrderStatus.PENDING;
        this.delieveredOnTime = DelieveredOnTime.NULL;
        this.orderPrice = 0;
    }
    
    //constructor for approving receiving order to update table
    public Order(int orderNumber, String orderDateTime) {
        this.orderNumber = orderNumber;
        this.orderDateTime = orderDateTime;
    }
    

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }
    
    public String getRequestedDateTime() {
        return requestedDateTime;
    }
    
    public int getIsEarlyOrder() {
        return isEarlyOrder;
    }
    
    

    public int getRestaurantNumber() {
        return orderNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    
    public int getCustomerNumber() {
    	return customerNumber;
    }
    
    public int getOrderPrice() {
    	return orderPrice;
    }
    
    public void setOrderPrice(int orderPrice) {
    	this.orderPrice = orderPrice;
    }
    
    public OrderStatus getOrderStatus() {
    	return orderStatus;
    }
    
    
    public void setOrderStatus(OrderStatus orderStatus) {
    	this.orderStatus = orderStatus;
    }
    
    public DelieveredOnTime getDeliveryTime() {
    	return delieveredOnTime;
    }
    
    public void setDeliveryTime(DelieveredOnTime delieveredOnTime) {
    	this.delieveredOnTime = delieveredOnTime;
    }
    

}