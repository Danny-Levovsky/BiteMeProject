package customer;

import client.Client;
import client.ClientController;
import login.LoginScreenController;
import entites.Message;
import entites.User;
import enums.Commands;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;




/**
 * The CustomerController class handles the customer UI actions and interactions.
 * This includes initializing the customer window and handling button actions for 
 * logging out, creating new orders, and viewing existing orders to receiving them
 * @author yosra
 */
public class CustomerController {

	@FXML
	private Label txtCustomerName;

	@FXML
	private Button btnLogout;

	@FXML
	private Button BtnNewOrder;

	@FXML
	private Button BtnViewOrder;
	
    @FXML
    private Label txtLocked;

	private static User customer;

	/**
     * Sets the customer for the controller.
     * @param user the customer user
     */
	public static void setCustomer(User user) {
		customer = user;
	}

	/**
     * Starts and displays the customer window.
     * @param primaryStage the primary stage for this application
     * @throws Exception if there is an error during the loading of the FXML file
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/Customer.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("CustomerWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Initializes the controller class. This method is automatically called after the
	 * FXML file has been loaded.
	 * It sets the visibility of the locked label to false, updates the customer name label if the customer is not null,
	 * sets the current instance of CustomerController in the Client class, and sends a message to the server to check the customer's status.
	 */
	@FXML
	private void initialize() {
		txtLocked.setVisible(false);
		// update name
		if (customer != null) {
			txtCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
		}
		
		Client.customerController = this;
		
		Message msg = new Message(customer.getId(), Commands. CheckStatus);
        ClientController.client.handleMessageFromClientControllers(msg);
	}
	
	/**
	 * Handles the response from the server.
	 * If the command in the message is CheckStatus, it processes the response.
	 * If the response indicates that the account is locked, it disables the new order and view order buttons
	 * and sets the visibility of the locked label to true.
	 * @param message the message received from the server
	 */
	 public void handleServerResponse(Message message) {
		 if (message.getCmd() == Commands. CheckStatus) {
    		 Object response = message.getObj();
    		 if (response instanceof String) {
                 String responseStr = (String) response;
                 if (responseStr.equals("locked")) {
                	 BtnNewOrder.setDisable(true);
                	 BtnViewOrder.setDisable(true); 
                	 txtLocked.setVisible(true);
                	 
                 }
    		 }
		 }
	 }

	/**
     * Handles the action for the logout button.
     * This method is triggered when the logout button is clicked.
     * It sends a logout request to the server and opens the login screen.
     * @param event the event triggered by the logout button click
     * @throws Exception if there is an error while opening the login screen
     */
	@FXML
	void getBtnLogout(ActionEvent event) throws Exception {
		Message logoutMessage = new Message(customer.getId(), Commands.LogoutUser);
		ClientController.client.handleMessageFromClientControllers(logoutMessage);

		((Node) event.getSource()).getScene().getWindow().hide();
		LoginScreenController newScreen = new LoginScreenController();
		newScreen.start(new Stage());
	}

	@FXML
    void getBtnNewOrder(ActionEvent event) throws Exception {
		NewOrderController.setCustomer(customer);
		((Node) event.getSource()).getScene().getWindow().hide();
		NewOrderController newScreen = new NewOrderController();
		newScreen.start(new Stage());
    }
	

	/**
     * Handles the action for the view order button.
     * This method is triggered when the view order button is clicked.
     * It hides the current window and opens the view order screen.
     * @param event the event triggered by the view order button click
     * @throws Exception if there is an error while opening the view order screen
     */
	@FXML
	void getBtnViewOrder(ActionEvent event) throws Exception {
		ViewOrderController.setId(customer.getId());
		((Node) event.getSource()).getScene().getWindow().hide();
		ViewOrderController newScreen = new ViewOrderController();
		newScreen.start(new Stage());

	}

}
