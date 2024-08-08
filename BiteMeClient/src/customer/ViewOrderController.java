package customer;

import client.Client;
import client.ClientController;
import entites.Message;
import entites.Order;
import enums.Commands;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;




/**
 * The ViewOrderController class handles the view order UI actions and interactions.
 * This includes initializing the view order window, fetching and displaying orders,
 * and handling button actions for navigating back to the customer screen and marking orders as received.
 * 
 *@author
 */
public class ViewOrderController {

	@FXML
	private Button btnBack;

	@FXML
	private Button btnReceived;

	@FXML
	private Label txtMsg;

	@FXML
	private TextField txtId1; // customer inserts order id here

	@FXML
	private TableView<Order> table;

	@FXML
	private TableColumn<Order, Integer> txtId;

	@FXML
	private TableColumn<Order, String> txtDate;

	@FXML
	private Label txtEmpty;

	private int orderId; // for saving the order Id that customer wants to approve receiving
	private static int id; // customer id
	private String receivedDateTime;
	
	 //private String dateTime; //to store the date we get from DB private int
	 //private int isEarlyOrder; 
	 //private int price;
	

	/**
	 * Sets the ID for the view order controller.
	 * @param id1 the ID to set
	 */
	public static void setId(int id1) {
		id = id1;
	}

	/**
	 * Starts and displays the view order window.
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if there is an error during the loading of the FXML file
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/ViewOrder.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ViewOrderWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
     * Initializes the controller class. This method is automatically called after the
     * FXML file has been loaded.
     * It sets up the table columns, hides the empty label, and fetches orders for the current customer.
     */
	@FXML
	private void initialize() {
		Client.viewOrderController = this; // Set the viewOrderController instance here

		txtEmpty.setVisible(false);

		txtId.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
		txtDate.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
		try {
			fetchOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetches pending orders for the current customer and populates the table.
	 * @throws Exception
	 */
	private void fetchOrders() throws Exception {
		Message msg = new Message(id, Commands.getPendingOrders);
		ClientController.client.sendToServer(msg);
	}

	/**
	 * Updates the table with the list of orders.
	 * @param orders the list of orders to display
	 */
	public void updateOrderTable(List<Order> orders) {
		ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
		table.setItems(orderList);
		if (table.getItems().isEmpty()) {
			txtEmpty.setVisible(true);
			btnReceived.setDisable(true);
			txtId1.setDisable(true);
		}
	}

	 /**
     * Displays a message on the UI.
     * @param msg the message to display
     */
	public void appearingMsg(String msg) {
		txtMsg.setText(msg);
	}

	 /**
     * Handles the action for the received button.
     * This method is triggered when the received button is clicked.
     * It verifies the order ID entered by the customer, sends a request to the server to update the order status,
     * and refreshes the table.
     * @param event the event triggered by the received button click
     * @throws Exception if there is an error while sending the request to the server
     */
	@FXML
	void getBtnReceived(ActionEvent event) throws Exception {
		try {
			orderId = Integer.parseInt(txtId1.getText());
			boolean orderExists = false;
			for (Order order : table.getItems()) {
				if (order.getOrderNumber() == orderId) {
					orderExists = true;
					break;
				}
			}
			if (!orderExists) {
				appearingMsg("wrong order id number");
			} else {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				receivedDateTime = LocalDateTime.now().format(dtf);

				Message msg = new Message(new Object[] { orderId, receivedDateTime },
						Commands.UpdateCustomerOrdersStatus);
				ClientController.client.sendToServer(msg);

				txtId1.clear();

				fetchOrders(); // refresh table
			}
		} catch (NumberFormatException e) {
			appearingMsg("you can only insert an integer");
		}
	}

	/**
     * Handles the response from the server.
     * This method processes the server response to determine if the order was late
     * and calculates any credit to be applied to the customer's account.
     * @param isEarlyOrder flag indicating if the order is an early order
     * @param dateTime the date and time the order was placed
     * @param totalPrice the total price of the order
     * @throws Exception if there is an error while processing the response
     */
	public void handleServerResponse(int isEarlyOrder, String dateTime, int totalPrice) throws Exception {
		/*
		 * this.isEarlyOrder = isEarlyOrder; this.dateTime = dateTime; this.price =
		 * totalPrice;
		 */
		int credit = 0;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTimeObj = LocalDateTime.parse(dateTime, dtf);
		LocalDateTime receivedDateTimeObj = LocalDateTime.parse(receivedDateTime, dtf);

		long minutesDifference = Duration.between(dateTimeObj, receivedDateTimeObj).toMinutes();

		if (isEarlyOrder == 0) {
			if (minutesDifference > 60) {
				credit = (int) (0.5 * totalPrice);
			}
		} else if (isEarlyOrder == 1) {
			if (minutesDifference > 20) {
				credit = (int) (0.5 * totalPrice);
			}
		}

		if (credit > 0) {
			Message msg = new Message(new Object[] { id, orderId, credit }, Commands.UpdateCredit);
			ClientController.client.sendToServer(msg);
			appearingMsg("sorry for being late you account is credited with " + credit + " NIS");
		}
	}

	/**
	 * Handles the action for the back button. This method is triggered when the
	 * back button is clicked. It hides the current window and opens the customer
	 * screen.
	 *
	 * @param event the event triggered by the back button click
	 * @throws Exception if there is an error while opening the customer screen
	 */
	@FXML
	void getBtnBack(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		CustomerController newScreen = new CustomerController();
		newScreen.start(new Stage());
	}

}
