package JDBC;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entites.Customer;
import entites.Order;
import entites.User;




/**
 * Controller class for database operations related to user management.
 * @author yosra
 */
public class DbController {
	
	private Connection conn;

    /**
     * Constructs a DbController with the given database connection.
     * @param connection the database connection
     */
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
    
    /**
     * Checks if a username exists in the database.
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
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
    
    /**
     * Checks if the provided password matches the stored password for the given username.
     * @param username the username to check
     * @param password the password to verify
     * @return true if the password is correct, false otherwise
     */
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
    
    /**
     * Retrieves user details from the database based on the username.
     * @param username the username of the user
     * @return a User object containing user details, or null if the user is not found
     */
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
    
    /**
     * Updates the login status of a user in the database.
     * @param userId the ID of the user
     * @param status the new login status (1 for logged in, 0 for logged out)
     */
    public void updateLoginStatus(int userId, int status) {
        String query = "UPDATE users SET IsLoggedIn = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if a user ID exists in the users table in database.
     * @param userId the user ID to check
     * @return true if the user ID exists, false otherwise
     */
    public boolean isUserIdExists(int userId) {
        String query = "SELECT COUNT(*) FROM users WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Retrieves the user details for a given user ID.
     * @param userId the user ID to retrieve details for
     * @return a User object containing the user's details, or null if the user ID does not exist or an error occurs
     */
    public User getUserDetailsById(int userId) {
        String query = "SELECT * FROM users WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("ID");
                String username = rs.getString("UserName");
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
    
    /**
     * Retrieves the status of a customer for a given user ID from customers table in database.
     * @param userId the user ID of the customer
     * @return the status of the customer as a String, or null if the user ID does not exist or an error occurs
     */
    public String getCustomerStatus(int userId) {
        String query = "SELECT Status FROM customers WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the status of a customer for a given user ID in customers table in database.
     * @param userId the user ID of the customer
     * @param status the new status to set for the customer
     */
    public void updateCustomerStatus(int userId, String status) {
        String query = "UPDATE customers SET Status = ? WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retrieves a list of restaurant names from the database.
     * @return An ArrayList of restaurant names.
     */
    public ArrayList<String> getRestaurantNamesFromDB() {
        ArrayList<String> restaurantNames = new ArrayList<>();
        String query = "SELECT RestaurantName FROM restaurants";
        
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String restaurantName = rs.getString("RestaurantName");
                restaurantNames.add(restaurantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom exception
        }
        
        return restaurantNames;
    }

    
		


    /**
     * Retrieves a list of pending orders for a specific customer.
     * @param customerId The ID of the customer.
     * @return A List of Order objects representing pending orders.
     */
    public List<Order> getPendingOrders(int customerId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT o.OrderID, o.OrderDateTime FROM orders o " +
                       "JOIN customer_orders co ON o.OrderID = co.OrderID " +
                       "JOIN customers c ON o.CustomerNumber = c.CustomerNumber " +
                       "WHERE c.ID = ? AND co.Status = 'pending'";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("OrderID");
                String orderDateTime = rs.getString("OrderDateTime");
                orders.add(new Order(orderId, orderDateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public ArrayList<Map<String, Object>> getRestaurantMenuFromDB(String restaurantName) {
        ArrayList<Map<String, Object>> menu = new ArrayList<>();
        String SQL_QUERY =
            "SELECT c.name AS dishType, d.dishID AS dishID, d.DishName AS dishName, " +
            "p.Price AS dishPrice, p.Size AS dishSize, do.OptionType, do.OptionValue " +
            "FROM dishes d " +
            "JOIN categories c ON d.CategoryID = c.CategoryID " +
            "JOIN prices p ON d.dishID = p.dishID " +
            "LEFT JOIN dish_options do ON d.dishID = do.dishID " +
            "WHERE d.RestaurantNumber = ( " +
            "  SELECT RestaurantNumber " +
            "  FROM restaurants " +
            "  WHERE RestaurantName = ? " +
            ")";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_QUERY)) {
            stmt.setString(1, restaurantName);
            ResultSet rs = stmt.executeQuery();

            Map<String, Map<String, Object>> dishMap = new HashMap<>();

            while (rs.next()) {
                String dishID = rs.getString("dishID");
                String dishSize = rs.getString("dishSize");
                int dishPrice = rs.getInt("dishPrice");

                Map<String, Object> dish = dishMap.computeIfAbsent(dishID, k -> {
                    Map<String, Object> newDish = new HashMap<>();
                    try {
                        newDish.put("dishID", dishID);
                        newDish.put("dishType", rs.getString("dishType"));
                        newDish.put("dishName", rs.getString("dishName"));
                        newDish.put("dishPrice", dishPrice);
                        newDish.put("dishOptions", new HashMap<String, List<String>>());
                        newDish.put("dishPrices", new HashMap<String, Integer>());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return newDish;
                });

                // Handle size options, ignoring 'Regular'
                if (dishSize != null && !dishSize.isEmpty() && !dishSize.equalsIgnoreCase("Regular")) {
                    Map<String, List<String>> options = (Map<String, List<String>>) dish.get("dishOptions");
                    options.computeIfAbsent("Size", k -> new ArrayList<>()).add(dishSize);
                    
                    // Store price for each size
                    Map<String, Integer> prices = (Map<String, Integer>) dish.get("dishPrices");
                    prices.put(dishSize, dishPrice);
                }

                String optionType = rs.getString("OptionType");
                String optionValue = rs.getString("OptionValue");
                
                if (optionType != null && optionValue != null) {
                    Map<String, List<String>> options = (Map<String, List<String>>) dish.get("dishOptions");
                    
                    if (optionType.equals("cooking_level") && optionValue.startsWith("M, MW, WD")) {
                        options.computeIfAbsent("Doneness", k -> new ArrayList<>())
                               .addAll(Arrays.asList("Medium", "Medium Well", "Well Done"));
                    } else if (optionType.equals("ingredient") && optionValue.startsWith("no ")) {
                        String ingredient = optionValue.substring(3); // remove "no " prefix
                        options.computeIfAbsent("Remove", k -> new ArrayList<>())
                               .add(ingredient);
                    } else {
                        options.computeIfAbsent(optionType, k -> new ArrayList<>())
                               .add(optionValue);
                    }
                }
            }

            menu.addAll(dishMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menu;
    }
    
    /**
     * Retrieves customer details from the database for a given user ID.
     * @param userID The ID of the user.
     * @return A Customer object containing the customer's details, or null if not found.
     */
    public Customer getCustomerFromDB(int userID) {
        Customer customer = null;
        String query = "SELECT CustomerNumber, ID, Credit, IsBusiness, Status " +
                       "FROM customers WHERE ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer(
                        rs.getInt("CustomerNumber"),
                        rs.getInt("ID"),
                        rs.getInt("Credit"),
                        rs.getBoolean("IsBusiness"),
                        rs.getString("Status")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider logging the error or throwing a custom exception
        }

        return customer;
    }
    	
    
    
   
//    public ArrayList<Map<String, Object>> getRestaurantMenuFromDB(String restaurantName) {
//        ArrayList<Map<String, Object>> menu = new ArrayList<>();
//        String SQL_QUERY =
//            "SELECT c.name AS dishType, d.dishID AS dishID, d.DishName AS dishName, p.Price AS dishPrice, do.OptionType, do.OptionValue " +
//            "FROM dishes d " +
//            "JOIN categories c ON d.CategoryID = c.CategoryID " +
//            "JOIN prices p ON d.dishID = p.dishID " +
//            "LEFT JOIN dish_options do ON d.dishID = do.dishID " +
//            "WHERE d.RestaurantNumber = ( " +
//            "  SELECT RestaurantNumber " +
//            "  FROM restaurants " +
//            "  WHERE RestaurantName = ? " +
//            ")";
//
//        try (PreparedStatement stmt = conn.prepareStatement(SQL_QUERY)) {
//            stmt.setString(1, restaurantName);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Map<String, Object> dish = new HashMap<>();
//                dish.put("dishID", rs.getString("dishID"));
//                dish.put("dishType", rs.getString("dishType"));
//                dish.put("dishName", rs.getString("dishName"));
//                dish.put("dishPrice", rs.getDouble("dishPrice"));
//
//                Map<String, String> options = new HashMap<>();
//                String optionType = rs.getString("OptionType");
//                String optionValue = rs.getString("OptionValue");
//                if (optionType != null && optionValue != null) {
//                    options.put(optionType, optionValue);
//                }
//                dish.put("dishOptions", options);
//
//                menu.add(dish);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return menu;
//    }

    /**
     * Imports external data into the application database.
     * This method performs multiple data import and update operations on the users, customers,
     * restaurants, and employee tables in bite_me DB
     */
    public void importExternalData() {
        try {

            // Import data from user_management.users to bite_me.users
            String importUsersQuery = "INSERT INTO bite_me.users (ID, UserName, Password, FirstName, LastName, Email, Phone, Type, IsLoggedIn, District) "
                    + "SELECT ID, UserName, Password, FirstName, LastName, Email, Phone, Type, IsLoggedIn, District FROM user_management.users";
            PreparedStatement importStmt = conn.prepareStatement(importUsersQuery);
            importStmt.executeUpdate();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
}