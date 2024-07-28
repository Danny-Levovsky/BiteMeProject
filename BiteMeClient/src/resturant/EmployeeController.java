package resturant;

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
    private Button btnOrderRecieved;

    @FXML
    private Button btnOrderCompleted;

    @FXML
    private TableView<?> txtTable;

    @FXML
    private TextField txtOrderID;

    @FXML
    private Label txtRestaurantName; //must replace with restaurant name 
    
    
    @FXML
    private Button btnBack;  //appear if I am certified employee 

    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnOrderCompleted(ActionEvent event) {

    }

    @FXML
    void getBtnOrderRecieved(ActionEvent event) {

    }

    @FXML
    void updateName(MouseEvent event) {   //update name for employee + restaurant name 

    }
    
    
    @FXML
    void getBtnBack(ActionEvent event) {

    }

}
