package JDBC;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Statement;

import entites.Category;
import entites.Customer;
import entites.DishUpdate;
import entites.DishOption;
import enums.OptionType;
import entites.Order;
import entites.Price;
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
        String queryOrders = "SELECT o.OrderID, o.CustomerNumber, o.IsDelivery, o.OrderDateTime, o.StatusRestaurant, d.DishName, r.Quantity, r.Size, r.Specification " +
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
                    restaurantOrder.setSpecification(rsOrders.getString("Specification"));
                    restaurantOrder.setSize(rsOrders.getString("Size"));

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
    
    
    public int[] getOrderReport(String district, int restaurantNumber, String monthYear) {
        // Step 1: Check if the report already exists in the order_reports table
        String checkReportQuery = "SELECT Salad, MainCourse, Dessert, Drink FROM order_reports WHERE MonthYear = ? AND District = ? AND RestaurantNumber = ?";
        
        try (PreparedStatement checkReportStmt = conn.prepareStatement(checkReportQuery)) {
            checkReportStmt.setString(1, monthYear);
            checkReportStmt.setString(2, district);
            checkReportStmt.setInt(3, restaurantNumber);

            ResultSet rs = checkReportStmt.executeQuery();
            if (rs.next()) {
                // Report exists, return the existing data
                int salad = rs.getInt("Salad");
                int mainCourse = rs.getInt("MainCourse");
                int dessert = rs.getInt("Dessert");
                int drink = rs.getInt("Drink");
                return new int[]{salad, mainCourse, dessert, drink};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Step 2: If the report does not exist, calculate it from the orders table
        String query = "SELECT SUM(o.Salad) AS SaladTotal, SUM(o.MainCourse) AS MainCourseTotal, " +
                       "SUM(o.Dessert) AS DessertTotal, SUM(o.Drink) AS DrinkTotal " +
                       "FROM orders o " +
                       "JOIN customers c ON o.CustomerNumber = c.CustomerNumber " +
                       "JOIN users u ON c.ID = u.ID " +
                       "WHERE u.District = ? AND c.Status = 'active' AND o.RestaurantNumber = ? " +
                       "AND DATE_FORMAT(o.OrderDateTime, '%c/%Y') = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, district);
            stmt.setInt(2, restaurantNumber);
            stmt.setString(3, monthYear);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int salad = rs.getInt("SaladTotal");
                int mainCourse = rs.getInt("MainCourseTotal");
                int dessert = rs.getInt("DessertTotal");
                int drink = rs.getInt("DrinkTotal");

                // Step 2.1: Insert or update the new report in the order_reports table
                String insertOrUpdateReportQuery = "INSERT INTO order_reports (MonthYear, District, RestaurantNumber, Salad, MainCourse, Dessert, Drink) " +
                                                   "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                                                   "ON DUPLICATE KEY UPDATE Salad = VALUES(Salad), MainCourse = VALUES(MainCourse), " +
                                                   "Dessert = VALUES(Dessert), Drink = VALUES(Drink)";

                try (PreparedStatement insertOrUpdateStmt = conn.prepareStatement(insertOrUpdateReportQuery)) {
                    insertOrUpdateStmt.setString(1, monthYear);
                    insertOrUpdateStmt.setString(2, district);
                    insertOrUpdateStmt.setInt(3, restaurantNumber);
                    insertOrUpdateStmt.setInt(4, salad);
                    insertOrUpdateStmt.setInt(5, mainCourse);
                    insertOrUpdateStmt.setInt(6, dessert);
                    insertOrUpdateStmt.setInt(7, drink);
                    insertOrUpdateStmt.executeUpdate();
                }

                return new int[]{salad, mainCourse, dessert, drink};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return zeros if no data found
        return new int[]{0, 0, 0, 0};
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
    
    public int[] performanceReport(String monthYear, String district) {
        // Step 1: Check if the report already exists in the performance_reports table
        String checkReportQuery = "SELECT Week1, Week2, Week3, Week4 FROM performance_reports WHERE MonthYear = ? AND District = ?";

        try (PreparedStatement checkReportStmt = conn.prepareStatement(checkReportQuery)) {
            checkReportStmt.setString(1, monthYear);
            checkReportStmt.setString(2, district);

            ResultSet rs = checkReportStmt.executeQuery();
            
            if (rs.next()) {
                // Report exists, return the existing data
                return new int[]{
                    rs.getInt("Week1"),
                    rs.getInt("Week2"),
                    rs.getInt("Week3"),
                    rs.getInt("Week4")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Step 2: If the report does not exist, calculate it from the orders table
        String query = "SELECT " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 1 AND 7 THEN 1 ELSE 0 END) AS TotalWeek1, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 1 AND 7 AND IsLate = 1 THEN 1 ELSE 0 END) AS LateWeek1, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 8 AND 14 THEN 1 ELSE 0 END) AS TotalWeek2, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 8 AND 14 AND IsLate = 1 THEN 1 ELSE 0 END) AS LateWeek2, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 15 AND 21 THEN 1 ELSE 0 END) AS TotalWeek3, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) BETWEEN 15 AND 21 AND IsLate = 1 THEN 1 ELSE 0 END) AS LateWeek3, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) >= 22 THEN 1 ELSE 0 END) AS TotalWeek4, " +
            "SUM(CASE WHEN DAYOFMONTH(OrderDateTime) >= 22 AND IsLate = 1 THEN 1 ELSE 0 END) AS LateWeek4 " +
            "FROM orders o " +
            "JOIN customers c ON o.CustomerNumber = c.CustomerNumber " +
            "JOIN users u ON c.ID = u.ID " +
            "WHERE u.District = ? AND c.Status = 'active' " +
            "AND DATE_FORMAT(o.OrderDateTime, '%c/%Y') = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, district);
            stmt.setString(2, monthYear);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int[] performanceData = new int[4];
                for (int i = 1; i <= 4; i++) {
                    int total = rs.getInt("TotalWeek" + i);
                    int late = rs.getInt("LateWeek" + i);
                    performanceData[i-1] = total > 0 ? (late * 100 / total) : 0;
                }
                System.out.println("Calculated performance data: " + Arrays.toString(performanceData));

                // Insert or update the new report in the performance_reports table
                String insertOrUpdateReportQuery = "INSERT INTO performance_reports (MonthYear, District, Week1, Week2, Week3, Week4) " +
                    "VALUES (?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE Week1 = VALUES(Week1), Week2 = VALUES(Week2), " +
                    "Week3 = VALUES(Week3), Week4 = VALUES(Week4)";

                try (PreparedStatement insertOrUpdateStmt = conn.prepareStatement(insertOrUpdateReportQuery)) {
                    insertOrUpdateStmt.setString(1, monthYear);
                    insertOrUpdateStmt.setString(2, district);
                    insertOrUpdateStmt.setInt(3, performanceData[0]);
                    insertOrUpdateStmt.setInt(4, performanceData[1]);
                    insertOrUpdateStmt.setInt(5, performanceData[2]);
                    insertOrUpdateStmt.setInt(6, performanceData[3]);
                    
                    insertOrUpdateStmt.executeUpdate();
                }

                return performanceData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return zeros if no data found
        return new int[]{0, 0, 0, 0};
    }

    /**
     * Retrieves a list of dishes associated with the restaurant of the given user.
     * The list includes details about the dish, category, options, and prices.
     * 
     * @param user The user whose restaurant's dishes are to be retrieved.
     * @return A list of objects representing the dishes, options, prices, and categories. 
     *         Each element in the list is an array of objects where:
     *         - index 0 is the {@link DishUpdate} object,
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
                DishUpdate dish = new DishUpdate(dishId, restaurantNumber, categoryId, dishName);
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
     * @param dish   The {@link DishUpdate} object representing the dish to be added.
     * @param price  The {@link Price} object representing the price of the dish.
     * @param option The {@link DishOption} object representing the option for the dish, or null if no option is available.
     * @return {@code true} if the dish was successfully added, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the insertion fails.
     */
    public boolean addDish(DishUpdate dish, Price price, DishOption option) {
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
     * @param dish   The {@link DishUpdate} object representing the dish.
     * @param price  The {@link Price} object representing the price to be added.
     * @param option The {@link DishOption} object representing the option to be added, or null if no option is available.
     * @return {@code true} if the price and option were successfully added, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the insertion fails.
     */
    public boolean insertPriceAndOption(DishUpdate dish, Price price, DishOption option) {
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
     * @return The {@link DishUpdate} object representing the dish found, or null if no matching dish is found.
     * @throws SQLException If a database access error occurs.
     */
    public DishUpdate findDishByNameAndSize(String dishName, String size) {
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
                return new DishUpdate(dishID, restaurantNumber, categoryId, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Deletes the specified dish from the database, including associated dish options, prices, and references in restaurants orders.
     * 
     * @param dish The {@link DishUpdate} object representing the dish to be deleted.
     * @return {@code true} if the dish was successfully deleted, {@code false} otherwise.
     * @throws SQLException If a database access error occurs or the deletion fails.
     */
    public boolean deleteDish(DishUpdate dish) {
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
    
    public ArrayList<Map<String, Object>> getRestaurantMenuFromDB(String restaurantName) {
        ArrayList<Map<String, Object>> menu = new ArrayList<>();
        String SQL_QUERY =
            "SELECT c.name AS dishType, d.dishID AS dishID, d.DishName AS dishName, " +
            "p.Price AS dishPrice, p.Size AS dishSize, do.OptionType, do.OptionValue, " +
            "r.BeginUpdate, r.EndUpdate, r.RestaurantNumber " +  // Added RestaurantNumber
            "FROM dishes d " +
            "JOIN categories c ON d.CategoryID = c.CategoryID " +
            "JOIN prices p ON d.dishID = p.dishID " +
            "LEFT JOIN dish_options do ON d.dishID = do.dishID " +
            "JOIN restaurants r ON d.RestaurantNumber = r.RestaurantNumber " +
            "WHERE d.RestaurantNumber = ( " +
            "  SELECT RestaurantNumber " +
            "  FROM restaurants " +
            "  WHERE RestaurantName = ? " +
            ")";

        try (PreparedStatement stmt = conn.prepareStatement(SQL_QUERY)) {
            stmt.setString(1, restaurantName);
            ResultSet rs = stmt.executeQuery();

            Map<String, Map<String, Object>> dishMap = new HashMap<>();
            Map<String, Object> restaurantInfo = new HashMap<>();  // To store BeginUpdate, EndUpdate, and RestaurantNumber
            
            boolean firstRow = true;
            while (rs.next()) {
                String dishID = rs.getString("dishID");
                String dishSize = rs.getString("dishSize");
                int dishPrice = rs.getInt("dishPrice");

                // Store BeginUpdate, EndUpdate, and RestaurantNumber only once
                if (restaurantInfo.isEmpty()) {
                	int restaurantNumber = rs.getInt("RestaurantNumber");
                    java.sql.Timestamp beginUpdate = rs.getTimestamp("BeginUpdate");
                    java.sql.Timestamp endUpdate = rs.getTimestamp("EndUpdate");
                    
                    restaurantInfo.put("RestaurantNumber", restaurantNumber);
                    restaurantInfo.put("BeginUpdate", beginUpdate);
                    restaurantInfo.put("EndUpdate", endUpdate);
                    
                    System.out.println("DbController: Retrieved RestaurantNumber: " + restaurantNumber);
                    System.out.println("DbController: Retrieved BeginUpdate: " + beginUpdate);
                    System.out.println("DbController: Retrieved EndUpdate: " + endUpdate);
                }

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
            // Add restaurantInfo as the last item in the menu list
            menu.add(restaurantInfo);
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