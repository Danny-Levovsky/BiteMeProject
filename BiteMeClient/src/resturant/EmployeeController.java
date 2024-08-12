package resturant;

import java.util.List;
import client.Client;
import client.ClientController;
import entites.Message;
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
import javafx.stage.Stage;
import login.LoginScreenController;

/**
 * The EmployeeController class handles the restaurant employee UI actions and
 * interactions. This includes initializing the employee window, fetching and
 * displaying orders, and handling button actions for logging out, receiving
 * orders, completing orders, and navigating back to the certified employee
 * screen.
 * @author yosra
 */
public class EmployeeController {

	@FXML
	private Label txtEmployeeName;

	@FXML
	private Button btnLogout; 

	@FXML
	private Button btnOrderReceived;

	@FXML
	private Button btnOrderCompleted;

	@FXML
	private TableView<RestaurantOrder> orderTable;
	@FXML
	private TableColumn<RestaurantOrder, Integer> orderIdColumn;
	@FXML
	private TableColumn<RestaurantOrder, Integer> customerNumberColumn;
	@FXML
	private TableColumn<RestaurantOrder, String> orderDateTimeColumn;

	@FXML
	private TableColumn<RestaurantOrder, String> dishNameColumn;
	@FXML
	private TableColumn<RestaurantOrder, Integer> quantityColumn;
	@FXML
	private TableColumn<RestaurantOrder, Integer> IsDeliveryColumn;
	@FXML
	private TableColumn<RestaurantOrder, String> orderStatusColumn;
	
	@FXML
    private TableColumn<RestaurantOrder, String> sizeColumn;
    @FXML
    private TableColumn<RestaurantOrder, String> specificationColumn;

	@FXML
	private TextField txtOrderID;

	@FXML
	private Button btnBack;

	@FXML
	private TextField txtTime;

	@FXML
	private Label txtMsg;

	private static User employee;

	private String btnPressed;

	private int isDelivery;

	/**
	 * Sets the employee for the controller.
	 * @param user the employee user
	 */
	public static void setEmployee(User user) {
		employee = user;
	}

	/**
	 * Starts and displays the employee window.
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if there is an error during the loading of the FXML file
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/Employee.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("EmployeeWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	 /**
     * Initializes the controller class. This method is automatically called after
     * the FXML file has been loaded. It updates the name to this user's name and
     * sets up the table columns and data.
     */
	@FXML
	private void initialize() {
		Client.employeeController = this; // Set the employeeController instance here
		
		btnOrderReceived.setDisable(true);
		btnOrderCompleted.setDisable(true);

		// update name
		if (employee != null) {
			txtEmployeeName.setText(employee.getFirstName() + " " + employee.getLastName());
		}
		
		// Show or hide btnBack based on user type
        if (employee.getType().equals("Certified Employee")) {
            btnBack.setVisible(true);
            btnLogout.setVisible(false);
        } else {
            btnBack.setVisible(false);
            btnLogout.setVisible(true);
        }

		orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
		customerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
		orderDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
		dishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
		specificationColumn.setCellValueFactory(new PropertyValueFactory<>("specification"));
		IsDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("IsDelivery"));
		orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		getTableData();
		
	}	

	/**
	 * Fetches the table data with current restaurant orders according to the
	 * employee ID.
	 */
	public void getTableData() {
		// get Table view data with current Restaurant orders according to employee id
		Commands command = Commands.getRestaurantOrders;
		Message message = new Message(employee.getId(), command);
		ClientController.client.handleMessageFromClientControllers(message);
	}

	/**
	 * Updates the table with the list of restaurant orders.
	 * @param restaurantOrders the list of restaurant orders to display
	 */
	public void setTable(List<RestaurantOrder> restaurantOrders) {
		ObservableList<RestaurantOrder> orderList = FXCollections.observableArrayList(restaurantOrders);
		orderTable.setItems(orderList);
	}



	/**
	 * Updates the status of an order.
	 * @param msg the message containing order ID and status
	 */
	public void updateOrderStatus(Object msg) {
		Commands command = Commands.updateRestaurantOrderStatus;
		Message message = new Message(msg, command);
		ClientController.client.handleMessageFromClientControllers(message);
		getTableData(); // refresh
		fetchCustomerDetails();
		btnOrderReceived.setDisable(true);
		btnOrderCompleted.setDisable(true);
	}

	/**
     * Handles the action for the order received button. This method is triggered
     * when the order received button is clicked. It updates the order status to
     * 'received' if it is currently pending.
     * @param event the event triggered by the order received button click
     */
	@FXML
	public void getBtnOrderReceived(ActionEvent event) {
		int orderId = Integer.parseInt(txtOrderID.getText());
		btnPressed = "received";
		Object[] idAndStatus = { orderId, btnPressed };
		for (RestaurantOrder order : orderTable.getItems()) {
			if (Integer.valueOf(order.getOrderId()).equals(orderId)) {
				if (String.valueOf(order.getOrderStatus()).equals("pending")) {
					updateOrderStatus(idAndStatus);
				} else {
					txtMsg.setText("order status is already received");
				}
				break;
			}
		}
	}

	  /**
     * Handles the action for the order completed button. This method is triggered
     * when the order completed button is clicked. It updates the order status to
     * 'completed' if it is currently received, and fetches the customer details.
     * @param event the event triggered by the order completed button click
     */
	@FXML
	public void getBtnOrderCompleted(ActionEvent event) {
		int orderId = Integer.parseInt(txtOrderID.getText());
		for (RestaurantOrder order : orderTable.getItems()) {
			if (Integer.valueOf(order.getOrderId()).equals(orderId)) {
				isDelivery = order.getIsDelivery();
			}
		}
		btnPressed = "completed";
		Object[] idAndStatus = { orderId, btnPressed };
		for (RestaurantOrder order : orderTable.getItems()) {
			if (Integer.valueOf(order.getOrderId()).equals(orderId)) {
				if (String.valueOf(order.getOrderStatus()).equals("received")) {
					updateOrderStatus(idAndStatus);
				} else {
					txtMsg.setText("first you need to approve receiving");
				}
				break;
			}
		}
	}

	/**
	 * Fetches the customer details based on the selected order.
	 */
	public void fetchCustomerDetails() {
		// Retrieve the customer details
		int orderId = Integer.parseInt(txtOrderID.getText());
		int coustomerIdToContact = getCoustomerIdToContactFromTableViewByOrderId(orderId); // customer number
		updateCoustomerToContactByCoustomerId(coustomerIdToContact);

	}

	/**
	 * Gets the customer ID to contact from the table view by order ID.
	 * @param orderId the order ID
	 * @return the customer number associated with the order, or -1 if not found
	 */
	private int getCoustomerIdToContactFromTableViewByOrderId(int orderId) {
		// Iterate through the orders in the orderTable
		for (RestaurantOrder order : orderTable.getItems()) {
			if (Integer.valueOf(order.getOrderId()).equals(orderId)) {
				// Return the customer number associated with the order
				return order.getCustomerNumber();
			}
		}
		// If no matching order is found
		return -1;
	}

	/**
	 * Updates the customer to contact by customer ID. 
	 * @param customerNumber the customer number
	 */
	private void updateCoustomerToContactByCoustomerId(int customerNumber) {
		Commands command = Commands.updateCoustomerToContactByCoustomerId;
		Message message = new Message(customerNumber, command);
		ClientController.client.handleMessageFromClientControllers(message);
	}

	/**
	 * Sends a text message and email to the customer.
	 * @param customer the customer to contact
	 */
	public void sendTextMassageAndEmailToCustomer(User customer) {
		// Determine the message based on the button pressed
		String additionalMessage = "";
		if (btnPressed.equals("received")) {
			additionalMessage = "Restaurant got your order.";
		} else if (btnPressed.equals("completed")) {
			if (isDelivery == 1) {
				additionalMessage = "Restaurant finished working on your order. " + "estimated arrival time "
						+ txtTime.getText();
			} else {
				additionalMessage = "Restaurant finished working on your order and ready for pickup.";
			}
		}
		// Create and show the alert
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notification Sent");
		alert.setHeaderText("Simulation: Text Message and Email Sent to Customer");
		alert.setContentText(String.format(
				"Text Message and Email sent to:%n" + "Name: %s %s%n" + "Phone: %s%n" + "Email: %s%n%n" + "%s%n", 
				customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getEmail(),
				additionalMessage));
		alert.showAndWait();
	}

	/**
	 * Handles the action for the back button. This method is triggered when the
	 * back button is clicked. It hides the current window and opens the certified
	 * employee screen.
	 * @param event the event triggered by the back button click
	 * @throws Exception if there is an error while opening the certified employee
	 *                   screen
	 */
	@FXML
	public void getBtnBack(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
		newScreen.start(new Stage());
	}
	
	/**
	 * Handles the action for the logout button. This method is triggered when the
	 * logout button is clicked. It sends a logout request to the server and opens
	 * the login screen.
	 * @param event the event triggered by the logout button click
	 * @throws Exception if there is an error while opening the login screen
	 */
	@FXML
	public void getBtnLogout(ActionEvent event) throws Exception {
		Message logoutMessage = new Message(EmployeeController.employee.getId(), Commands.LogoutUser);
		ClientController.client.handleMessageFromClientControllers(logoutMessage);

		((Node) event.getSource()).getScene().getWindow().hide();
		LoginScreenController newScreen = new LoginScreenController();
		newScreen.start(new Stage());
	}
	
	/**
     * Enables the received and completed buttons when an order ID is entered.
     * @param event the event triggered by entering an order ID
     */
    @FXML
    public void getTxtOrderID(ActionEvent event) {
		btnOrderReceived.setDisable(false);
		btnOrderCompleted.setDisable(false);
    }
}
