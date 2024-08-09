package entites;

//Class based on DB's Table
//Employee should inherit from User? Need to discuss.

public class Employee{
    private int employeeNumber;
    private int restaurantNumber;
    private int id;

    public Employee(int employeeNumber, int id, int restaurantNumber) {
        this.id = id; 
        this.employeeNumber = employeeNumber;
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