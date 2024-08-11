package JDBC;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import entites.Category;
import entites.Dish;
import entites.DishOption;
//import entites.Option.OptionType;
import entites.Price;
import entites.Restaurant;
import entites.User;
import enums.OptionType;

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
    
    
    
    
    /**
     * Retrieves a list of dishes associated with the restaurant of the given user.
     * The list includes details about the dish, category, options, and prices.
     * 
     * @param user The user whose restaurant's dishes are to be retrieved.
     * @return A list of objects representing the dishes, options, prices, and categories. 
     *         Each element in the list is an array of objects where:
     *         - index 0 is the {@link Dish} object,
     *         - index 1 is the {@link DishOption} object,
     *         - index 2 is the {@link Price} object,
     *         - index 3 is the {@link Category} object.
     */   
    public List<Object[]> getDishesByCertifiedEmployee(User user) {
        List<Object[]> dishes = new ArrayList<>();
        String query = "SELECT d.DishID, d.RestaurantNumber, d.CategoryId, d.DishName, " +
                       "c.Name AS CategoryName, " +
                       "do.OptionType, do.OptionValue, " +
                       "p.Size, p.Price " +
                       "FROM dishes d " +
                       "LEFT JOIN categories c ON d.CategoryId = c.CategoryID " +
                       "LEFT JOIN dish_options do ON d.DishID = do.DishID " +
                       "JOIN prices p ON d.DishID = p.DishID " +
                       "JOIN employee e ON d.RestaurantNumber = e.RestaurantNumber " +
                       "WHERE e.ID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int dishId = rs.getInt("DishID");
                int restaurantNumber = rs.getInt("RestaurantNumber");
                int categoryId = rs.getInt("CategoryId");
                String dishName = rs.getString("DishName");
                String categoryName = rs.getString("CategoryName");
                Dish dish = new Dish(dishId, restaurantNumber, categoryId, dishName);
                Category category = new Category(categoryId, categoryName);  
                String optionTypeStr = rs.getString("OptionType");
                String optionValue = rs.getString("OptionValue");
                DishOption option = null;
                if (optionTypeStr != null) {
                    try {
                        OptionType optionType = OptionType.valueOf(optionTypeStr.toUpperCase());
                        option = new DishOption(dishId, optionType, optionValue);
                    } catch (IllegalArgumentException e) {
                        // Log the error and continue processing
                        System.err.println("Invalid OptionType: " + optionTypeStr);
                    }
                }
                String size = rs.getString("Size");
                int price = rs.getInt("Price");
                Price priceObj = new Price(dishId, size, price);

                dishes.add(new Object[]{dish, option, priceObj, category});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    
    /**
     * Retrieves the restaurant number associated with the given user.
     * 
     * @param user The user whose restaurant number is to be retrieved.
     * @return The restaurant number if found, or -1 if no restaurant is found.
     * @throws SQLException If a database access error occurs or the user is not associated with a restaurant.
     */
    public int getRestaurantNum(User user) {
        String query = "SELECT e.RestaurantNumber " +
                       "FROM employee e " +
                       "WHERE e.ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("RestaurantNumber");
            } else {
                throw new SQLException("No restaurant number found for user ID: " + user.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // No Restaurant found
        }
    }
    
    
    /**
     * Retrieves the restaurant name associated with the given user.
     * 
     * @param user The user whose restaurant name is to be retrieved.
     * @return The restaurant name if found, or null if no restaurant is found or an error occurs.
     * @throws SQLException If a database access error occurs or the user is not associated with a restaurant.
     */
    public String getRestaurantName(User user) {
        String query = "SELECT r.RestaurantName " +
                       "FROM restaurants r " +
                       "JOIN employee e ON r.RestaurantNumber = e.RestaurantNumber " +
                       "WHERE e.ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RestaurantName");
            } else {
                throw new SQLException("No restaurant name found for user ID: " + user.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // In case of an error, return null
        }
    }
    
    
    /**
     * Adds a new dish to the database, along with its price and optional options.
     * 
     * @param dish   The {@link Dish} object representing the dish to be added.
     * @param price  The {@link Price} object representing the price of the dish.
     * @param option The {@link DishOption} object representing the option for the dish, or null if no option is available.
     * @return {@code true} if the dish was successfully added, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the insertion fails.
     */
    public boolean addDish(Dish dish, Price price, DishOption option) {
        String insertDishQuery = "INSERT INTO dishes (RestaurantNumber, CategoryID, DishName) VALUES (?, ?, ?)";
        String insertPriceQuery = "INSERT INTO prices (DishID, Size, Price) VALUES (?, ?, ?)";
        String insertOptionQuery = "INSERT INTO dish_options (DishID, OptionType, OptionValue) VALUES (?, ?, ?)";
        try (PreparedStatement dishStmt = conn.prepareStatement(insertDishQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement priceStmt = conn.prepareStatement(insertPriceQuery);
             PreparedStatement optionStmt = conn.prepareStatement(insertOptionQuery)) {

            // Insert dish
            dishStmt.setInt(1, dish.getRestaurantNumber());
            dishStmt.setInt(2, dish.getCategoryId());
            dishStmt.setString(3, dish.getDishName());
            int affectedRows = dishStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating dish failed, no rows affected.");
            }

            try (ResultSet generatedKeys = dishStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int dishId = generatedKeys.getInt(1);

                    // Insert price
                    priceStmt.setInt(1, dishId);
                    priceStmt.setString(2, price.getSize());
                    priceStmt.setInt(3, price.getPrice());
                    priceStmt.executeUpdate();

                    // Insert option if available
                    if (option != null) {
                        optionStmt.setInt(1, dishId);
                        optionStmt.setString(2, option.getOptionType().toString());
                        optionStmt.setString(3, option.getOptionValue());
                        optionStmt.executeUpdate();
                    }
                    return true;
                } else {
                    throw new SQLException("Creating dish failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Inserts a new price and option for the given dish into the database.
     * 
     * @param dish   The {@link Dish} object representing the dish.
     * @param price  The {@link Price} object representing the price to be added.
     * @param option The {@link DishOption} object representing the option to be added, or null if no option is available.
     * @return {@code true} if the price and option were successfully added, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the insertion fails.
     */
    public boolean insertPriceAndOption(Dish dish, Price price, DishOption option) {
        String insertPriceQuery = "INSERT INTO prices (DishID, Size, Price) VALUES (?, ?, ?)";
        String insertOptionQuery = "INSERT INTO dish_options (DishID, OptionType, OptionValue) VALUES (?, ?, ?)";
        try (PreparedStatement priceStmt = conn.prepareStatement(insertPriceQuery);
             PreparedStatement optionStmt = conn.prepareStatement(insertOptionQuery)) {

            // Insert price
            priceStmt.setInt(1, dish.getDishID());
            priceStmt.setString(2, price.getSize());
            priceStmt.setInt(3, price.getPrice());
            priceStmt.executeUpdate();

            // Insert option if available
            if (option != null) {
                optionStmt.setInt(1, dish.getDishID());
                optionStmt.setString(2, option.getOptionType().toString());
                optionStmt.setString(3, option.getOptionValue());
                optionStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Checks if a dish with the given name and size already exists in the database.
     * 
     * @param dishName The name of the dish to check.
     * @param size     The size of the dish to check.
     * @return {@code true} if a dish with the specified name and size exists, {@code false} otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean isDishExists(String dishName, String size) {
        String query = "SELECT * FROM dishes d JOIN prices p ON d.DishID = p.DishID WHERE d.DishName = ? AND p.Size = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dishName);
            stmt.setString(2, size);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * Finds a dish by its name and size, ensuring that the size is different from the one specified.
     * 
     * @param dishName The name of the dish to find.
     * @param size     The size of the dish to exclude.
     * @return The {@link Dish} object representing the dish found, or null if no matching dish is found.
     * @throws SQLException If a database access error occurs.
     */
    public Dish findDishByNameAndSize(String dishName, String size) {
        String query = "SELECT * FROM dishes WHERE DishName = ? AND DishID IN (SELECT DishID FROM prices WHERE Size != ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dishName);
            stmt.setString(2, size);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int dishID = rs.getInt("DishID");
                int restaurantNumber = rs.getInt("RestaurantNumber");
                int categoryId = rs.getInt("CategoryID");
                String name = rs.getString("DishName");
                return new Dish(dishID, restaurantNumber, categoryId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Deletes the specified dish from the database, including associated dish options, prices, and references in restaurants orders.
     * 
     * @param dish The {@link Dish} object representing the dish to be deleted.
     * @return {@code true} if the dish was successfully deleted, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the deletion fails.
     */
    public boolean deleteDish(Dish dish) {
        String updateOrdersQuery = "UPDATE restaurants_orders SET DishID = NULL WHERE DishID = ?";
        String deleteDishOptionsQuery = "DELETE FROM dish_options WHERE DishID = ?";
        String deletePricesQuery = "DELETE FROM prices WHERE DishID = ?";
        String deleteDishQuery = "DELETE FROM dishes WHERE DishID = ?";

        try {
            // First, update restaurants_orders table
            try (PreparedStatement updateOrdersStmt = conn.prepareStatement(updateOrdersQuery)) {
                updateOrdersStmt.setInt(1, dish.getDishID());
                updateOrdersStmt.executeUpdate();
            }

            // Then, delete related entries in dish_options table
            try (PreparedStatement deleteDishOptionsStmt = conn.prepareStatement(deleteDishOptionsQuery)) {
                deleteDishOptionsStmt.setInt(1, dish.getDishID());
                deleteDishOptionsStmt.executeUpdate();
            }

            // Next, delete related entries in prices table
            try (PreparedStatement deletePricesStmt = conn.prepareStatement(deletePricesQuery)) {
                deletePricesStmt.setInt(1, dish.getDishID());
                deletePricesStmt.executeUpdate();
            }

            // Finally, delete the dish
            try (PreparedStatement deleteDishStmt = conn.prepareStatement(deleteDishQuery)) {
                deleteDishStmt.setInt(1, dish.getDishID());
                int affectedRows = deleteDishStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    /**
     * Updates the price of a dish in the database.
     * 
     * @param price The {@link Price} object containing the updated price information.
     * @return {@code true} if the price was successfully updated, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the update fails.
     */
    public boolean updateDishPrice(Price price) {
        String query = "UPDATE prices SET Price = ? WHERE DishID = ? AND Size = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, price.getPrice());
            stmt.setInt(2, price.getDishID());
            stmt.setString(3, price.getSize());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     
    
    /**
     * Updates the BeginUpdate timestamp for a specific restaurant in the database.
     * This method is called when a certified employee begins updating the menu.
     *
     * @param restaurantNum The unique identifier of the restaurant to update.
     * @param localTime     The Timestamp representing the time when the update began.
     *
     * @throws SQLException If there's an error executing the SQL statement.
     *
     */ 
    public void updateEntryTime(int restaurantNum, Timestamp localTime) {
        String query = "UPDATE restaurants SET BeginUpdate = ? WHERE RestaurantNumber = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Use setObject with Types.TIMESTAMP to ensure compatibility with datetime
            pstmt.setObject(1, localTime, Types.TIMESTAMP);
            pstmt.setInt(2, restaurantNum);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Successfully updated BeginUpdate for restaurant " + restaurantNum);
            } else {
                System.out.println("No restaurant found with number " + restaurantNum);
            }
        } catch (SQLException e) {
            System.err.println("Error updating BeginUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /**
     * Updates the EndUpdate timestamp for a specific restaurant in the database.
     * This method is called when a certified employee finishes updating the menu.
     *
     * @param restaurantNum The unique identifier of the restaurant to update.
     * @param localTime     The Timestamp representing the time when the update ended.
     *
     * @throws SQLException If there's an error executing the SQL statement.
     *
     */
    public void updateExitTime(int restaurantNum, Timestamp localTime) {
        String query = "UPDATE restaurants SET EndUpdate = ? WHERE RestaurantNumber = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Use setObject with Types.TIMESTAMP to ensure compatibility with datetime
            pstmt.setObject(1, localTime, Types.TIMESTAMP);
            pstmt.setInt(2, restaurantNum);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Successfully updated EndUpdate for restaurant " + restaurantNum);
            } else {
                System.out.println("No restaurant found with number " + restaurantNum);
            }
        } catch (SQLException e) {
            System.err.println("Error updating EndUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
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