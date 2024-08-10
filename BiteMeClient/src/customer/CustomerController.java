package customer;

import client.ClientController;
import login.ConnectionScreenController;
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
     * it update the name to this user's name
     */
	@FXML
	private void initialize() {
		// update name
		if (customer != null) {
			txtCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
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
		Message logoutMessage = new Message(CustomerController.customer.getId(), Commands.LogoutUser);
		ClientController.client.sendToServer(logoutMessage);

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
