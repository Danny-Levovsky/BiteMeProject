package entites;

import java.io.Serializable;


/**
 * Represents a Customer entity based on the corresponding database table.
 * This class implements Serializable for object serialization.
 * 
 * The Customer class may need to inherit from the User class if there is a shared structure.
 * This decision should be discussed further.
 * 
 * @author yosra
 */
public class Customer implements Serializable {
	
	/**
     * A unique identifier for the serialization runtime to associate with the Customer class.
     * This value ensures that a deserialized object is compatible with the current class definition.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The unique identifier for the customer.
     */
    private int customerNumber;
    
    /**
     * The ID of the user associated with this customer.
     */
    private int id;
    
    /**
     * The credit amount for the customer.
     */
    private int credit;
    
    /**
     * Indicates whether the customer is a business customer.
     */
    private boolean isBusiness;
    
    /**
     * The status of the customer (e.g., active, inactive).
     */
    private String status;

    /**
     * Constructs a new Customer object with the specified details.
     *
     * @param customerNumber The unique identifier for the customer.
     * @param id The ID of the user associated with this customer.
     * @param credit The credit amount for the customer.
     * @param isBusiness Indicates whether the customer is a business customer.
     * @param status The status of the customer.
     */
    public Customer(int customerNumber, int id, int credit, boolean isBusiness, String status) {
        this.customerNumber = customerNumber;
        this.id = id;
        this.credit = credit;
        this.isBusiness = isBusiness;
        this.status = status;
    }
    
    /**
     * Default constructor for the Customer class.
     */
    public Customer() {
    }

    /**
     * Returns the customer number.
     * 
     * @return The customer number.
     */
    public int getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Returns the ID of the user associated with this customer.
     * 
     * @return The user ID.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the credit amount for the customer.
     * 
     * @return The credit amount.
     */
    public int getCredit() {
        return credit;
    }

    /**
     * Returns whether the customer is a business customer.
     * 
     * @return {@code true} if the customer is a business customer, {@code false} otherwise.
     */
    public boolean isBusiness() {
        return isBusiness;
    }

    /**
     * Returns the status of the customer.
     * 
     * @return The status of the customer.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the customer number.
     * 
     * @param customerNumber The customer number to set.
     */
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * Sets the ID of the user associated with this customer.
     * 
     * @param id The user ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the credit amount for the customer.
     * 
     * @param credit The credit amount to set.
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }

    /**
     * Sets whether the customer is a business customer.
     * 
     * @param isBusiness {@code true} if the customer is a business customer, {@code false} otherwise.
     */
    public void setBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    /**
     * Returns the status of the customer.
     * 
     * @return The status of the customer.
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Returns a string representation of the Customer object.
     * 
     * @return A string representation of the Customer object.
     */
    @Override
    public String toString() {
        return "Customer{" +
               "customerNumber=" + customerNumber +
               ", id=" + id +
               ", credit=" + credit +
               ", isBusiness=" + isBusiness +
               ", status='" + status + '\'' +
               '}';
    }
}