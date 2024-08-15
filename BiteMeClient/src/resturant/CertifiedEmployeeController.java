package resturant;

import client.ClientController;
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
import login.LoginScreenController;

/**
 * Controller class for the Certified Employee window in a restaurant management system.
 * This class handles the UI interactions and logic for certified employees.
 * Certified employees can log out, update the menu, and view orders through this interface.
 * 
 * @author yosra
 */
public class CertifiedEmployeeController {
    @FXML
    private Label txtCertifiedEmployeeName;
    @FXML
    private Button btnLogout;
    @FXML
    private Button BtnUpdateMenu;
    @FXML
    private Button BtnViewOrders;

    /**
     * The User object representing the certified employee.
     */
    private static User certifiedEmployee;

    /**
     * Sets the certified employee for the controller.
     * 
     * @param employee The User object representing the certified employee
     */
    public static void setCertifiedEmployee(User employee) {
        certifiedEmployee = employee;
    }
    
    
    /**
     * Initializes and displays the Certified Employee window.
     * 
     * @param primaryStage The primary stage for this JavaFX application
     * @throws Exception If there's an error loading the FXML or initializing the window
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/CertifiedEmployee.fxml"));
    	Parent root = loader.load();
        CertifiedEmployeeController controller = loader.getController();
        controller.initializeUser();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("CertifiedEmployeeWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
	/**
     * Initializes the user interface with the current certified employee's information.
     * This method is called during the setup of the Certified Employee window to display
     * the employee's name on the screen.
     */
    private void initializeUser() {
        if (certifiedEmployee != null) {
            txtCertifiedEmployeeName.setText(certifiedEmployee.getFirstName() + " " + certifiedEmployee.getLastName());
        }
    }
    
    /**
     * Handles the logout action when the logout button is clicked.
     * This method sends a logout message to the server, hides the current window,
     * and transitions back to the login screen.
     * 
     * @param event The action event triggered by clicking the logout button
     * @throws Exception If there's an error during the logout process or opening the login screen
     */
    @FXML
    public void getBtnLogout(ActionEvent event) throws Exception {
		Message logoutMessage = new Message(certifiedEmployee.getId(), Commands.LogoutUser);
		ClientController.client.handleMessageFromClientControllers(logoutMessage);
    	
    	((Node) event.getSource()).getScene().getWindow().hide();
    	LoginScreenController newScreen = new LoginScreenController();
    	newScreen.start(new Stage());
    }

    
    /**
     * Handles the action to update the menu when the update menu button is clicked.
     * This method hides the current window and opens the Update Menu screen for the certified employee.
     * 
     * @param event The action event triggered by clicking the update menu button
     * @throws Exception If there's an error opening the update menu screen
     */
    @FXML
    public void getBtnUpdateMenu(ActionEvent event) throws Exception {
    	UpdateMenuController.setCertifiedEmployee(certifiedEmployee);
    	((Node) event.getSource()).getScene().getWindow().hide();
    	UpdateMenuController.getInstance().start(new Stage());
    }
    

    /**
     * Handles the action to view orders when the view orders button is clicked.
     * This method hides the current window and opens the Employee screen to view orders.
     * 
     * @param event The action event triggered by clicking the view orders button
     * @throws Exception If there's an error opening the employee screen
     */
    @FXML
    public void getBtnVieOrders(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
        EmployeeController.setEmployee(certifiedEmployee);
        EmployeeController employeeScreen = new EmployeeController();
		employeeScreen.start(new Stage());
    }

}
