package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entites.Order;

public class DbController {
	private Connection conn;

    // Constructor that takes a SqlConnection object
    public DbController( Connection connection ) {
        this.conn = connection ;
    }
    public Object getRestaurantOrders(Object obj) {
		String restaurantName = (String) obj;
		
		List<Order> restaurantOrders = new ArrayList<>();
        
        String query = "SELECT o.OrderID, o.CustomerNumber, r.RestaurantName, ro.Status, o.RestaurantNumber" +
                       "FROM orders o " +
                       "JOIN restaurants r ON o.RestaurantNumber = r.RestaurantNumber " +
                       "JOIN restaurants_orders ro ON o.OrderID = ro.OrderID " +
                       "WHERE r.RestaurantName = ? AND ro.Status = 'PENDING' OR 'RECEIVED'";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, restaurantName);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int orderNumber = rs.getInt("OrderID");
                int customerNumber = rs.getInt("CustomerNumber");
                int restaurantNumber = rs.getInt("RestaurantNumber");
                String fetchedRestaurantName = rs.getString("RestaurantName");
                String orderStatus = rs.getString("Status");

                Order order = new Order(orderNumber, fetchedRestaurantName, restaurantNumber, customerNumber); 
                order.setOrderStatus(Order.OrderStatus.valueOf(orderStatus));

                restaurantOrders.add(order);
            }
            
            return restaurantOrders;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
            	
}
