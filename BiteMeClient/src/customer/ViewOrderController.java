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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ViewOrderController {

    @FXML
    private Button btnBack;

    @FXML
    private Label txtMsg;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> txtId;

    @FXML
    private TableColumn<Order, String> txtDate;

    private static int id;

    /**
     * Sets the ID for the view order controller.
     *
     * @param id1 the ID to set
     */
    public static void setId(int id1) {
        id = id1;
    }

    /**
     * Starts and displays the view order window.
     *
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

    @FXML
    private void initialize() {
        Client.viewOrderController = this; // Set the viewOrderController instance here
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
     *
     * @param orders the list of orders to display
     */
    public void updateOrderTable(List<Order> orders) {
        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
        table.setItems(orderList);
    }

    public void appearingMsg(String msg) {
        txtMsg.setText(msg);
    }

    @FXML
    void getBtnApprove(ActionEvent event) {
        // Handle approval action
    }

    /**
     * Handles the action for the back button.
     * This method is triggered when the back button is clicked.
     * It hides the current window and opens the customer screen.
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
