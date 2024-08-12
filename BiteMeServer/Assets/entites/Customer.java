package entites;

import java.io.Serializable;

//Class based on DB's Table
//Customer should inherit from User? Need to discuss

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private int customerNumber;
    private int id;
    private int credit;
    private boolean isBusiness;
    private String status;

    public Customer(int customerNumber, int id, int credit, boolean isBusiness, String status) {
        this.customerNumber = customerNumber;
        this.id = id;
        this.credit = credit;
        this.isBusiness = isBusiness;
        this.status = status;
    }
    
    public Customer() {
    }

    // Existing getters
    public int getCustomerNumber() {
        return customerNumber;
    }

    public int getId() {
        return id;
    }

    public int getCredit() {
        return credit;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public String getStatus() {
        return status;
    }

    // New setters
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
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