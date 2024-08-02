package entites;

//Class based on DB's Table
//Employee should inherit from User? Need to discuss.

public class Employee {
    private int employeeNumber;
    private int id;
    private int restaurantNumber;
    
    public Employee(int employeeNumber, int id, int restaurantNumber) {
        this.employeeNumber = employeeNumber;
        this.id = id;
        this.restaurantNumber = restaurantNumber;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public int getId() {
        return id;
    }

    public int getRestaurantNumber() {
        return restaurantNumber;
    }
}