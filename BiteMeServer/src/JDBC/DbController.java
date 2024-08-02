package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entites.User;

public class DbController {
	private Connection conn;

    // Constructor that takes a SqlConnection object
    public DbController( Connection connection ) {
        this.conn = connection ;
    }
    /*public Object getRestaurantPendingOrders(Object obj) {
		String restaurantName = (String) obj;
		
    	ArrayList<ManagerRequestDetail> requestList = new ArrayList<>();
	    String query = "SELECT parkName, changeTo, amountTo, requestNumber, changes FROM managerrequest";
	    try {
	         PreparedStatement stmt = conn.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery(); 

	        while (rs.next()) {
	            String parkName = rs.getString("parkName");
	            String changeTo = rs.getString("changeTo");
	            String amountTo = rs.getString("amountTo");
	            int requestNumber = rs.getInt("requestNumber");
	            String changes = rs.getString("changes");

	            ManagerRequestDetail requestDetail = new ManagerRequestDetail(parkName, changeTo, amountTo);
	            requestDetail.setRequestNumber(requestNumber);
	            requestList.add(requestDetail);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return requestList;
	    	
    }*/
    
    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE UserName = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isPasswordCorrect(String username, String password) {
        String query = "SELECT Password FROM users WHERE UserName = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("Password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public User getUserDetails(String username) {
        String query = "SELECT * FROM users WHERE UserName = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                String password = rs.getString("Password");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                String type = rs.getString("Type");
                int isLoggedIn = rs.getInt("IsLoggedIn");
                String district = rs.getString("District");

                return new User(id, username, password, firstName, lastName, email, phone, type, isLoggedIn, district);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public void importExternalData() {
        try {
            // Clear the target table
            String clearUsersTableQuery = "TRUNCATE TABLE bite_me.users";
            PreparedStatement clearUsersTableStmt = conn.prepareStatement(clearUsersTableQuery);
            clearUsersTableStmt.executeUpdate();

            // Import data from user_management.users to bite_me.users
            String importUsersQuery = "INSERT INTO bite_me.users (ID, UserName, Password, FirstName, LastName, Email, Phone, Type, IsLoggedIn, District) "
                    + "SELECT ID, UserName, Password, FirstName, LastName, Email, Phone, Type, IsLoggedIn, District FROM user_management.users";
            PreparedStatement importStmt = conn.prepareStatement(importUsersQuery);
            importStmt.executeUpdate();

            // Create customers table
            String createCustomersTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.customers ("
                    + "CustomerNumber INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ID INT, "
                    + "Credit INT DEFAULT 0, "
                    + "IsBusiness TINYINT, "
                    + "PaymentCardNumber INT, "
                    + "PaymentCardDate VARCHAR(10), "
                    + "Status VARCHAR(10) DEFAULT 'locked', "
                    + "FOREIGN KEY (ID) REFERENCES users(ID))";
            PreparedStatement createCustomersTableStmt = conn.prepareStatement(createCustomersTableQuery);
            createCustomersTableStmt.executeUpdate();

            // Insert into customers table
            String insertCustomersQuery = "INSERT INTO bite_me.customers (ID, IsBusiness, PaymentCardNumber, PaymentCardDate) VALUES "
                    + "(11, 0, 963852741, '07/2030'), "
                    + "(22, 0, 741564951, '03/2025'), "
                    + "(33, 0, 654123841, '01/2026'), "
                    + "(44, 1, 961782346, '06/2028'), "
                    + "(55, 0, 627183285, '04/2024'), "
                    + "(66, 1, 627175285, '04/2029'), "
                    + "(77, 0, 287694132, '06/2029'), "
                    + "(88, 0, 973508085, '07/2031'), "
                    + "(99, 0, 815374351, '11/2027'), "
                    + "(111, 0, 881596491, '12/2026'), "
                    + "(122, 0, 881596491, '08/2029'), "
                    + "(133, 0, 901174220, '01/2027'), "
                    + "(144, 1, 891718880, '11/2024'), "
                    + "(155, 0, 322264644, '02/2030'), "
                    + "(166, 0, 195431542, '05/2026')";
            PreparedStatement insertCustomersStmt = conn.prepareStatement(insertCustomersQuery);
            insertCustomersStmt.executeUpdate();

            // Update customers table
            String updateCustomersQuery1 = "UPDATE bite_me.customers SET Credit = 20, Status = 'active' WHERE ID = 11";
            PreparedStatement updateCustomersStmt1 = conn.prepareStatement(updateCustomersQuery1);
            updateCustomersStmt1.executeUpdate();

            String updateCustomersQuery2 = "UPDATE bite_me.customers SET Credit = 15, Status = 'active' WHERE ID = 66";
            PreparedStatement updateCustomersStmt2 = conn.prepareStatement(updateCustomersQuery2);
            updateCustomersStmt2.executeUpdate();

            String updateCustomersQuery3 = "UPDATE bite_me.customers SET Status = 'active' WHERE ID = 155";
            PreparedStatement updateCustomersStmt3 = conn.prepareStatement(updateCustomersQuery3);
            updateCustomersStmt3.executeUpdate();

            // Create restaurants table
            String createRestaurantsTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.restaurants ("
                    + "RestaurantNumber INT AUTO_INCREMENT PRIMARY KEY, "
                    + "RestaurantName VARCHAR(45) DEFAULT NULL, "
                    + "MenuID INT, "
                    + "District VARCHAR(45) DEFAULT NULL, "
                    + "FOREIGN KEY (MenuID) REFERENCES bite_me.menus(MenuID))";
            PreparedStatement createRestaurantsTableStmt = conn.prepareStatement(createRestaurantsTableQuery);
            createRestaurantsTableStmt.executeUpdate();

            // Insert into restaurants table
            String insertRestaurantsQuery = "INSERT INTO bite_me.restaurants (RestaurantName, MenuID, District) VALUES "
                    + "('The Savory Spoon Karmiel', 1, 'north'), "
                    + "('Bistro Belle Vie Haifa', 2, 'north'), "
                    + "('Harvest Moon Café Nahariyya', 3, 'north'), "
                    + "('The Savory Spoon Natanya', 1, 'center'), "
                    + "('Bistro Belle Vie Raanana', 2, 'center'), "
                    + "('Harvest Moon Café Rehovot', 3, 'center'), "
                    + "('The Savory Spoon Ashdod', 1, 'south'), "
                    + "('Bistro Belle Vie Eilat', 2, 'south'), "
                    + "('Harvest Moon Café Dimona', 3, 'south')";
            PreparedStatement insertRestaurantsStmt = conn.prepareStatement(insertRestaurantsQuery);
            insertRestaurantsStmt.executeUpdate();

            // Create employee table
            String createEmployeeTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.employee ("
                    + "EmployeeNumber INT AUTO_INCREMENT PRIMARY KEY, "
                    + "ID INT, "
                    + "RestaurantNumber INT, "
                    + "FOREIGN KEY (ID) REFERENCES bite_me.users(ID), "
                    + "FOREIGN KEY (RestaurantNumber) REFERENCES bite_me.restaurants(RestaurantNumber))";
            PreparedStatement createEmployeeTableStmt = conn.prepareStatement(createEmployeeTableQuery);
            createEmployeeTableStmt.executeUpdate();

            // Insert into employee table
            String insertEmployeeQuery = "INSERT INTO bite_me.employee (ID, RestaurantNumber) VALUES "
                    + "(10, 1), "
                    + "(20, 2), "
                    + "(30, 3), "
                    + "(40, 4), "
                    + "(50, 5), "
                    + "(60, 6), "
                    + "(70, 7), "
                    + "(80, 8), "
                    + "(90, 9), "
                    + "(100, 1), "
                    + "(200, 2), "
                    + "(300, 3), "
                    + "(400, 4), "
                    + "(500, 5), "
                    + "(600, 6), "
                    + "(700, 7), "
                    + "(800, 8), "
                    + "(900, 9)";
            PreparedStatement insertEmployeeStmt = conn.prepareStatement(insertEmployeeQuery);
            insertEmployeeStmt.executeUpdate();

            // Create orders table
            String createOrdersTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.orders ("
                    + "OrderID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "CustomerNumber INT, "
                    + "RestaurantNumber INT, "
                    + "TotalPrice INT, "
                    + "Salad INT, "
                    + "MainCourse INT, "
                    + "Dessert INT, "
                    + "Drink INT, "
                    + "IsDelivery TINYINT, "
                    + "IsEarlyOrder TINYINT, "
                    + "RequestedDateTime DATETIME, "
                    + "OrderDateTime DATETIME, "
                    + "ReceivedDateTime DATETIME, "
                    + "FOREIGN KEY (CustomerNumber) REFERENCES customers(CustomerNumber), "
                    + "FOREIGN KEY (RestaurantNumber) REFERENCES restaurants(RestaurantNumber))";
            PreparedStatement createOrdersTableStmt = conn.prepareStatement(createOrdersTableQuery);
            createOrdersTableStmt.executeUpdate();

            // Create customer_orders table
            String createCustomerOrdersTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.customer_orders ("
                    + "OrderID INT, "
                    + "Status VARCHAR(45), "
                    + "FOREIGN KEY (OrderID) REFERENCES orders(OrderID))";
            PreparedStatement createCustomerOrdersTableStmt = conn.prepareStatement(createCustomerOrdersTableQuery);
            createCustomerOrdersTableStmt.executeUpdate();

            // Create restaurants_orders table
            String createRestaurantsOrdersTableQuery = "CREATE TABLE IF NOT EXISTS bite_me.restaurants_orders ("
                    + "OrderID INT, "
                    + "Status VARCHAR(45), "
                    + "FOREIGN KEY (OrderID) REFERENCES orders(OrderID))";
            PreparedStatement createRestaurantsOrdersTableStmt = conn.prepareStatement(createRestaurantsOrdersTableQuery);
            createRestaurantsOrdersTableStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}