package entites;

import java.io.Serializable;

public class RestaurantOrder implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int orderID;
	private int customerNumber;
	private String orderDateTime;
	private String receivedDateTime;
	private String dishName;
	private int quantity;
	private int IsDelivery;
    private ResturantOrderStatus orderStatus;
    
    
    
    
    public enum ResturantOrderStatus{
    	PENDING, RECEIVED
    }
    
    public RestaurantOrder() {};    
    
    public RestaurantOrder(int orderID, int customerNumber, String orderDateTime, String dishName, int quantity, int IsDelivery, ResturantOrderStatus orderStatus) {
    	this.setOrderID(orderID);
    	this.setCustomerNumber(customerNumber);
    	this.setOrderDateTime(orderDateTime);
        this.setDishName(dishName);
        this.setQuantity(quantity);
        this.setIsDelivery(IsDelivery);
        this.setOrderStatus(orderStatus);
    }


	public int getOrderID() { return orderID; }
	public void setOrderID(int orderID) { this.orderID = orderID; }

	public int getCustomerNumber() { return customerNumber; }
	public void setCustomerNumber(int customerNumber) { this.customerNumber = customerNumber; }

	public String getOrderDateTime() { return orderDateTime; }
	public void setOrderDateTime(String orderDateTime) { this.orderDateTime = orderDateTime; }

	public String getReceivedDateTime() { return receivedDateTime; }
	public void setReceivedDateTime(String receivedDateTime) { this.receivedDateTime = receivedDateTime; }

	public String getDishName() { return dishName; }
	public void setDishName(String dishName) { this.dishName = dishName; }

	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }

	public int getIsDelivery() { return IsDelivery; }
	public void setIsDelivery(int isDelivery) { IsDelivery = isDelivery; }

	public ResturantOrderStatus getOrderStatus() { return orderStatus; }
	public void setOrderStatus(ResturantOrderStatus orderStatus) { this.orderStatus = orderStatus; }
    
    

}