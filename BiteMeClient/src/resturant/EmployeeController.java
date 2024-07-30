package resturant;

import client.ClientController;
import entites.Message;
import enums.Commands;
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

    void getTableData() {
    	// get Table view data with current Pending Restaurant orders according to Restaurant id
    	Commands command = Commands.getRestaurantPendingOrders;
    	Message message = new Message(txtRestaurantName, command);
    	ClientController.client.handleMessageFromClientControllers(message);
    }
    
    void setTable(Object msg) {
    	tableView = (TableView<?>) msg;
    }
    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnOrderCompleted(ActionEvent event) {
    	// send to server updateRestaurantOrderToStatus(Completed)
    }

    @FXML
    void getBtnOrderReceived(ActionEvent event) {
    	// send to server updateRestaurantOrderToStatus(Received)
    }

    @FXML
    void updateName(MouseEvent event) {   //update name for employee + restaurant name 

    }
    
    
    @FXML
    void getBtnBack(ActionEvent event) {

    }

}
