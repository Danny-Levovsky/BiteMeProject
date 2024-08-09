package resturant;

import client.ClientController;
import entites.Message;
import enums.Commands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.LoginScreenController;
import entites.User;

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
    
    private static User employee;

    public static void setEmployee(User user) {
        employee = user;
    }

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/Employee.fxml"));
    	Parent root = loader.load();
        EmployeeController controller = loader.getController();
        controller.initializeUser();
        
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("EmployeeWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
    private void initializeUser() {
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
    } 
    
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
    void getBtnLogout(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	LoginScreenController newScreen = new LoginScreenController();
    	newScreen.start(new Stage());
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
    void getBtnBack(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
    	newScreen.start(new Stage());

    }

}
