package resturant;

import client.ClientController;
import entites.Message;
import entites.Order;
import entites.Order.OrderStatus;
import enums.Commands;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    private TableView<?> tableView; // need to set entite to save data

    @FXML
    private TextField txtOrderID;

    @FXML
    private Label txtRestaurantName; //must replace with restaurant name 
    
    @FXML
    private Button btnBack;  //appear if I am certified employee 
    
    private ObservableList<Order> ordersData = FXCollections.observableArrayList();

    void getTableData() {
    	// get Table view data with current Pending Restaurant orders according to Restaurant id
    	Commands command = Commands.getRestaurantOrders;
    	Message message = new Message(txtRestaurantName, command);
    	ClientController.client.handleMessageFromClientControllers(message);
    }
    
    public void setTable(Object msg) {
    	ordersData.setAll((Order[]) msg);
    }
    
    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    void updateOrderStatus(Object msg) {
    	Commands command = Commands.updateOrderStatus;
    	Message message = new Message(msg, command);
    	ClientController.client.handleMessageFromClientControllers(message);
    }
    
    @FXML
    void getBtnOrderCompleted(ActionEvent event) {
    	Object[] idAndStatus = {txtOrderID, OrderStatus.READY};
    	updateOrderStatus(idAndStatus);
    }

    @FXML
    void getBtnOrderReceived(ActionEvent event) {
    	Object[] idAndStatus = {txtOrderID, OrderStatus.RECEIVED};
    	updateOrderStatus(idAndStatus);
    }
    
    
    @FXML
    void getBtnBack(ActionEvent event) {

    }

}
