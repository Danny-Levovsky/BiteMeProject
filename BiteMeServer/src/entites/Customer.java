package entites;

//Class based on DB's Table
//Customer should inherit from User? Need to discuss

public class Customer {
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
}