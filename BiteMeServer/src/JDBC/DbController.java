package JDBC;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import entites.Order;
import entites.RestaurantOrder;
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
    
    public int[] IncomeReport(int restaurantNumber, String monthYear, String district) {
        int[] incomeReportResultData = new int[4]; // Array to hold income data for 4 weeks

        System.out.println("Starting IncomeReport calculation for Restaurant: " + restaurantNumber + ", Month/Year: " + monthYear + ", District: " + district);

        // 1. Try to fetch data from the income_reports table
        String selectIncomeQuery = "SELECT Week1, Week2, Week3, Week4 FROM bite_me.income_reports WHERE RestaurantNumber = ? AND MonthYear = ? AND District = ?";
        
        try (PreparedStatement selectStmt = conn.prepareStatement(selectIncomeQuery)) {
            selectStmt.setInt(1, restaurantNumber);
            selectStmt.setString(2, monthYear);
            selectStmt.setString(3, district);
            
            ResultSet rs = selectStmt.executeQuery();
            
            if (rs.next()) {
                incomeReportResultData[0] = rs.getInt("Week1");
                incomeReportResultData[1] = rs.getInt("Week2");
                incomeReportResultData[2] = rs.getInt("Week3");
                incomeReportResultData[3] = rs.getInt("Week4");
                System.out.println("Data found in income_reports: " + Arrays.toString(incomeReportResultData));
                return incomeReportResultData; // Return early if data found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. If data not found in income_reports, calculate it from the orders table
        System.out.println("No data found in income_reports. Calculating from orders...");
        
        String selectCustomersQuery = "SELECT c.CustomerNumber FROM bite_me.customers c JOIN bite_me.users u ON c.ID = u.ID WHERE u.District = ? AND u.Type = 'customer' AND c.Status = 'active'";
        List<Integer> customerNumbers = new ArrayList<>();
        
        try (PreparedStatement selectCustomersStmt = conn.prepareStatement(selectCustomersQuery)) {
            selectCustomersStmt.setString(1, district);
            ResultSet rs = selectCustomersStmt.executeQuery();
            
            while (rs.next()) {
                customerNumbers.add(rs.getInt("CustomerNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (customerNumbers.isEmpty()) {
            System.out.println("No customers found in the specified district.");
            return incomeReportResultData; // If no customers found, return an empty result
        }

        System.out.println("Customer numbers found: " + customerNumbers);
        
        String placeholders = String.join(",", Collections.nCopies(customerNumbers.size(), "?"));
        
        String selectOrdersQuery = "SELECT TotalPrice, OrderDateTime FROM bite_me.orders WHERE RestaurantNumber = ? AND CustomerNumber IN (" + placeholders + ") AND DATE_FORMAT(OrderDateTime, '%c/%Y') = ?";
        
        try (PreparedStatement selectOrdersStmt = conn.prepareStatement(selectOrdersQuery)) {
            selectOrdersStmt.setInt(1, restaurantNumber);
            
            int i = 2;
            for (Integer customerNumber : customerNumbers) {
                selectOrdersStmt.setInt(i++, customerNumber);
            }
            
            selectOrdersStmt.setString(i, monthYear);

            ResultSet rs = selectOrdersStmt.executeQuery();
            
            while (rs.next()) {
                int totalPrice = rs.getInt("TotalPrice");
                LocalDateTime orderDateTime = rs.getTimestamp("OrderDateTime").toLocalDateTime();
                int weekOfMonth = orderDateTime.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
                System.out.println("OrderDateTime: " + orderDateTime + ", TotalPrice: " + totalPrice + ", WeekOfMonth: " + weekOfMonth);
                
                if (weekOfMonth >= 1 && weekOfMonth <= 4) {
                    incomeReportResultData[weekOfMonth - 1] += totalPrice;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Calculated incomeReportResultData: " + Arrays.toString(incomeReportResultData));

        // 3. Update the income_reports table with the calculated values
        String updateIncomeQuery = "INSERT INTO bite_me.income_reports (MonthYear, District, RestaurantNumber, Week1, Week2, Week3, Week4) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Week1 = ?, Week2 = ?, Week3 = ?, Week4 = ?";
        
        try (PreparedStatement updateStmt = conn.prepareStatement(updateIncomeQuery)) {
            updateStmt.setString(1, monthYear);
            updateStmt.setString(2, district);
            updateStmt.setInt(3, restaurantNumber);
            updateStmt.setInt(4, incomeReportResultData[0]);
            updateStmt.setInt(5, incomeReportResultData[1]);
            updateStmt.setInt(6, incomeReportResultData[2]);
            updateStmt.setInt(7, incomeReportResultData[3]);
            
            updateStmt.setInt(8, incomeReportResultData[0]);
            updateStmt.setInt(9, incomeReportResultData[1]);
            updateStmt.setInt(10, incomeReportResultData[2]);
            updateStmt.setInt(11, incomeReportResultData[3]);

            updateStmt.executeUpdate();
            System.out.println("Updated income_reports table with calculated values.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return incomeReportResultData;
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
     * Retrieves the pending orders for a given customer.
     * @param customerId the customer ID
     * @return a list of pending orders
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
    
    /**
     * Updates the status of an order to 'received' and records the received date and time.
     * Also retrieves the details of the order to calculate any credit if applicable.
     * @param orderId the order ID
     * @param receivedDateTime the date and time when the order was received
     * @return an array containing the early order flag, the relevant date and time, and the total price
     */
    public Object[] updateOrderStatus(int orderId, String receivedDateTime) {
        String updateCustomerOrdersQuery = "UPDATE customer_orders SET Status = 'received' WHERE OrderID = ?";
        String updateOrdersQuery = "UPDATE orders SET ReceivedDateTime = ? WHERE OrderID = ?";
        String selectOrderDetailsQuery = "SELECT IsEarlyOrder, RequestedDateTime, OrderDateTime, TotalPrice FROM orders WHERE OrderID = ?";
        
        int isEarlyOrder = 0;
        String dateTime = "";
        int totalPrice = 0;

        try {
            conn.setAutoCommit(false);

            PreparedStatement customerOrdersStmt = conn.prepareStatement(updateCustomerOrdersQuery);
            customerOrdersStmt.setInt(1, orderId);
            customerOrdersStmt.executeUpdate();

            PreparedStatement ordersStmt = conn.prepareStatement(updateOrdersQuery);
            ordersStmt.setString(1, receivedDateTime);
            ordersStmt.setInt(2, orderId);
            ordersStmt.executeUpdate();
            
            PreparedStatement selectOrderDetailsStmt = conn.prepareStatement(selectOrderDetailsQuery);
            selectOrderDetailsStmt.setInt(1, orderId);
            ResultSet rs = selectOrderDetailsStmt.executeQuery();
            
            if (rs.next()) {
                isEarlyOrder = rs.getInt("IsEarlyOrder");
                if (isEarlyOrder == 0) {
                    dateTime = rs.getString("OrderDateTime");
                } else {
                    dateTime = rs.getString("RequestedDateTime");
                }
                totalPrice = rs.getInt("TotalPrice");
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
        return new Object[]{isEarlyOrder, dateTime,totalPrice};
    }
    
    /**
     * Updates the credit of a customer based on the delay in order delivery.
     * @param id the customer ID
     * @param orderId the order ID
     * @param credit the amount of credit to be added
     */
    public void updateCredit(int id, int orderId, int credit) {
        String updateOrdersQuery = "UPDATE orders SET IsLate = 1 WHERE OrderID = ?";
        String selectCurrentCreditQuery = "SELECT Credit FROM customers WHERE ID = ?";
        String updateCustomersQuery = "UPDATE customers SET Credit = ? WHERE ID = ?";

        try {
            conn.setAutoCommit(false);

            // Update the orders table
            PreparedStatement ordersStmt = conn.prepareStatement(updateOrdersQuery);
            ordersStmt.setInt(1, orderId);
            ordersStmt.executeUpdate();

            // Get the current credit
            PreparedStatement selectCreditStmt = conn.prepareStatement(selectCurrentCreditQuery);
            selectCreditStmt.setInt(1, id);
            ResultSet rs = selectCreditStmt.executeQuery();
            int currentCredit = 0;
            if (rs.next()) {
                currentCredit = rs.getInt("Credit");
            }

            // Add the new credit to the current credit
            int newCredit = currentCredit + credit;

            // Update the customers table
            PreparedStatement customersStmt = conn.prepareStatement(updateCustomersQuery);
            customersStmt.setInt(1, newCredit);
            customersStmt.setInt(2, id);
            customersStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    /**
     * Updates the status of a restaurant order.
     * @param orderId the order ID
     * @param status the new status to set
     */
    public void updateRestaurantOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET StatusRestaurant = ? WHERE OrderID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retrieves the list of restaurant orders for a given employee.
     * @param employeeId the employee ID
     * @return a list of restaurant orders
     */
    public List<RestaurantOrder> getRestaurantOrders(int employeeId) {
        List<RestaurantOrder> restaurantOrders = new ArrayList<>();
        String queryRestaurantNumber = "SELECT RestaurantNumber FROM employee WHERE ID = ?";
        String queryOrders = "SELECT o.OrderID, o.CustomerNumber, o.IsDelivery, o.OrderDateTime, o.StatusRestaurant, d.DishName, r.Quantity " +
                "FROM orders o " +
                "JOIN restaurants_orders r ON o.OrderID = r.OrderID " +
                "JOIN dishes d ON r.DishID = d.DishID " +
                "WHERE o.RestaurantNumber = ? " +
                "AND (o.StatusRestaurant = 'pending' OR o.StatusRestaurant = 'received')";

        try (PreparedStatement stmtRestaurantNumber = conn.prepareStatement(queryRestaurantNumber)) {
            stmtRestaurantNumber.setInt(1, employeeId);
            ResultSet rsRestaurantNumber = stmtRestaurantNumber.executeQuery();
            int givenRestaurantNumber = 0;
            
            if (rsRestaurantNumber.next()) {
                givenRestaurantNumber = rsRestaurantNumber.getInt("RestaurantNumber");
            } else {
                // No matching restaurant number found, return empty list
                return restaurantOrders;
            }

            try (PreparedStatement stmtOrders = conn.prepareStatement(queryOrders)) {
                stmtOrders.setInt(1, givenRestaurantNumber);
                ResultSet rsOrders = stmtOrders.executeQuery();

                while (rsOrders.next()) {
                    RestaurantOrder restaurantOrder = new RestaurantOrder();
                    restaurantOrder.setOrderId(rsOrders.getInt("OrderID"));
                    restaurantOrder.setCustomerNumber(rsOrders.getInt("CustomerNumber"));
                    restaurantOrder.setIsDelivery(rsOrders.getInt("IsDelivery"));
                    restaurantOrder.setOrderDateTime(rsOrders.getString("OrderDateTime"));
                    restaurantOrder.setOrderStatus(rsOrders.getString("StatusRestaurant"));
                    restaurantOrder.setDishName(rsOrders.getString("DishName"));
                    restaurantOrder.setQuantity(rsOrders.getInt("Quantity"));

                    restaurantOrders.add(restaurantOrder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurantOrders;
    }
    
    /**
     * Retrieves the customer details by their customer number.
     * @param customerNumber the customer number
     * @return a User object containing the customer details, or null if the customer does not exist
     */
    public User getCustomerDetailsByNumber(int customerNumber) {
        String queryCustomerId = "SELECT ID FROM customers WHERE CustomerNumber = ?";
        String queryUserDetails = "SELECT * FROM users WHERE ID = ?";
        int userId = 0;

        try {
            // Step 1: Get the ID from customers table using CustomerNumber
            PreparedStatement stmtCustomerId = conn.prepareStatement(queryCustomerId);
            stmtCustomerId.setInt(1, customerNumber);
            ResultSet rsCustomerId = stmtCustomerId.executeQuery();
            if (rsCustomerId.next()) {
                userId = rsCustomerId.getInt("ID");
            } else {
                // No matching customer found
                return null;
            }

            // Step 2: Get the user details from users table using the fetched ID
            PreparedStatement stmtUserDetails = conn.prepareStatement(queryUserDetails);
            stmtUserDetails.setInt(1, userId);
            ResultSet rsUserDetails = stmtUserDetails.executeQuery();
            if (rsUserDetails.next()) {
                int id = rsUserDetails.getInt("ID");
                String username = rsUserDetails.getString("UserName");
                String password = rsUserDetails.getString("Password");
                String firstName = rsUserDetails.getString("FirstName");
                String lastName = rsUserDetails.getString("LastName");
                String email = rsUserDetails.getString("Email");
                String phone = rsUserDetails.getString("Phone");
                String type = rsUserDetails.getString("Type");
                int isLoggedIn = rsUserDetails.getInt("IsLoggedIn");
                String district = rsUserDetails.getString("District");

                return new User(id, username, password, firstName, lastName, email, phone, type, isLoggedIn, district);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
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
                    + "(11, 1, 963852741, '07/2030'), "
                    + "(22, 0, 741564951, '03/2025'), "
                    + "(33, 0, 654123841, '01/2026'), "
                    + "(44, 1, 961782346, '06/2028'), "
                    + "(55, 0, 627183285, '04/2024'), "
                    + "(66, 1, 627175285, '04/2029')";
              

            PreparedStatement insertCustomersStmt = conn.prepareStatement(insertCustomersQuery);
            insertCustomersStmt.executeUpdate();

            // Update customers table
            String updateCustomersQuery1 = "UPDATE bite_me.customers SET Credit = 20, Status = 'active' WHERE ID = 11";
            PreparedStatement updateCustomersStmt1 = conn.prepareStatement(updateCustomersQuery1);
            updateCustomersStmt1.executeUpdate();

            String updateCustomersQuery2 = "UPDATE bite_me.customers SET Credit = 15, Status = 'active' WHERE ID = 55";
            PreparedStatement updateCustomersStmt2 = conn.prepareStatement(updateCustomersQuery2);
            updateCustomersStmt2.executeUpdate();

            String updateCustomersQuery3 = "UPDATE bite_me.customers SET Status = 'active' WHERE ID = 33";
            PreparedStatement updateCustomersStmt3 = conn.prepareStatement(updateCustomersQuery3);
            updateCustomersStmt3.executeUpdate();


            // Insert into employee table
            String insertEmployeeQuery = "INSERT INTO bite_me.employee (ID, RestaurantNumber) VALUES "
                    + "(10, 1), "
                    + "(20, 2), "
                    + "(30, 3), "
                    + "(40, 4), "
                    + "(50, 5), "                    
                    + "(100, 1), "
                    + "(200, 2), "
                    + "(300, 3), "
                    + "(400, 4), "
                    + "(500, 5)";

            PreparedStatement insertEmployeeStmt = conn.prepareStatement(insertEmployeeQuery);
            insertEmployeeStmt.executeUpdate();       
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	
}