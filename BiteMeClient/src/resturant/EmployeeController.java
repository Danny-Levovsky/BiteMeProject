package resturant;

import java.util.ArrayList;

import client.ClientController;
import entites.Message;
import entites.Order;
import entites.Order.OrderStatus;
import entites.Restaurant;
import entites.RestaurantOrder;
import entites.User;
import enums.Commands;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.LoginScreenController;

public class EmployeeController {

	
    @FXML
    private Label txtEmployeeName; //must replace with employee name 

    @FXML
    private Button btnLogout;  //disappear if I'm certified employee

    @FXML
    private Button btnOrderReceived;

    @FXML
    private Button btnOrderCompleted;

    
    @FXML
    private TableView<RestaurantOrder> orderTable;
    @FXML
    private TableColumn<RestaurantOrder, Integer> orderIdColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> customerNumberColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> orderDateTimeColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> receivedDateTimeColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> dishNameColumn;
    @FXML
    private TableColumn<RestaurantOrder, Integer> quantityColumn;
    @FXML
    private TableColumn<RestaurantOrder, Integer> IsDeliveryColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> orderStatusColumn;
    
    
    @FXML
    private TextField txtOrderID;

    @FXML
    private Label txtRestaurantName; //must replace with restaurant name 
    
    private ObservableList<RestaurantOrder> ordersData = FXCollections.observableArrayList();
    
    private static User employee;
    private static Restaurant restaurant;
    private static RestaurantOrder restaurantOrder;
    private static User coustomerToContact;
    
    @FXML
    private Button btnBack;  //appear if I am certified employee 

    public static void setEmployee(User user) { employee = user; }
    public static void setRestaurant(Restaurant restaurant) { EmployeeController.restaurant = restaurant; }
    public static User getCoustomerToContact() { return coustomerToContact; }
    public static void setCoustomerToContact(User user) { EmployeeController.coustomerToContact = user; }
    
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/Employee.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("EmployeeWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
	/**
     * Initializes the controller class. This method is automatically called after the
     * FXML file has been loaded.
     * it update the name to this user's name
     */
	@FXML
	private void initialize() {
		// update name
		if (employee != null) {
			txtEmployeeName.setText(employee.getFirstName() + " " + employee.getLastName());
		}
		
		if (restaurant != null) {
			txtRestaurantName.setText(restaurant.getRestaurantName());
		}
		
		orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		customerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
		orderDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
		receivedDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("receivedDateTime"));
		dishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		IsDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("IsDelivery"));
		orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

		
		getTableData();
	}
	
    void getTableData() {
    	// get Table view data with current Restaurant orders according to Restaurant id
    	Commands command = Commands.getRestaurantOrders;
    	Message message = new Message(restaurant.getRestaurantNumber(), command);
    	ClientController.client.handleMessageFromClientControllers(message);
    }
    
    public void setTable(ArrayList<RestaurantOrder> restaurantOrders) {
        orderTable.getItems().setAll(restaurantOrders);
    }
    
    @FXML
    void getBtnLogout(ActionEvent event) throws Exception {
		Message logoutMessage = new Message(EmployeeController.employee.getId(), Commands.LogoutUser);
		ClientController.client.sendToServer(logoutMessage);

		((Node) event.getSource()).getScene().getWindow().hide();
		LoginScreenController newScreen = new LoginScreenController();
		newScreen.start(new Stage());
    }
    
    void updateOrderStatus(Object msg) {
    	Commands command = Commands.updateRestaurantOrderStatus;
    	Message message = new Message(msg, command);
    	ClientController.client.handleMessageFromClientControllers(message);
    	getTableData();
    }

    @FXML
    void getBtnOrderCompleted(ActionEvent event) {
    	Object[] idAndStatus = {txtOrderID, OrderStatus.READY};
    	updateOrderStatus(idAndStatus);
    	
    	//sendTextMassageAndEmailToCustomer();
    }
    /*
    void sendTextMassageAndEmailToCustomer() {
    	// Retrieve the order details
        String orderId = txtOrderID.getText();
        
        if (restaurantOrder != null) {
            // Retrieve customer details (assuming you have a method to get customer by ID)
            User customer = getCustomerById(order.getCustomerNumber());
            
            if (customer != null) {
                // Create and show the alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Notification Sent Simulation");
                alert.setHeaderText("Text Message and Email Sent to Customer");
                alert.setContentText(String.format("Text Message and Email sent to:%n" +
                                                   "Name: %s %s%n" +
                                                   "Phone: %s%n" +
                                                   "Email: %s%n" +
                                                   "Order ID: %s%n" +
                                                   "Status: Order is ready for pickup",
                                                   customer.getFirstName(), customer.getLastName(),
                                                   customer.getPhone(), customer.getEmail(),
                                                   orderId));
                alert.showAndWait();
            } else {
                showErrorAlert("Customer not found for the given order.");
            }
        } else {
            showErrorAlert("Order not found.");
        }
    }

	private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/
    
    @FXML
    void getBtnOrderReceived(ActionEvent event) {
    	Object[] idAndStatus = {txtOrderID, OrderStatus.RECEIVED};
    	updateOrderStatus(idAndStatus);
    }
    
    
    @FXML
    void getBtnBack(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
    	newScreen.start(new Stage());

    }

}
