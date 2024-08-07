package branch_manager;


import entites.User;
import enums.Commands;
import client.ClientController;
import entites.Message;
import login.LoginScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;




/**
 * Controller class for the branch manager interface.
 * @author yosra
 */
public class BranchManagerController {

    @FXML
    private Label txtBranchManagerName; 

    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnUpdateClient;

    @FXML
    private Button BtnViewReports;

    @FXML
    private ComboBox<Integer> monthComboBox;
    
    private static User branchManager;

    /**
    * Sets the branch manager user.
    * @param user the User object representing the branch manager
    */
    public static void setbranchManager(User user) {
        branchManager = user;
    }
    
    /**
     * Starts the branch manager interface.
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/branch_manager/BranchManager.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("BranchManagerWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
    
	
    @FXML
    private void initialize() {
        // Add month values 1 to 12 to the ComboBox
        for (int i = 1; i <= 12; i++) {
        	monthComboBox.getItems().add(i);
        }
        
        //update name
        if(branchManager != null) {
        	txtBranchManagerName.setText(branchManager.getFirstName() + " " + branchManager.getLastName());
        }
    }


    /**
     * Handles the logout button action. Sends a logout message to the server through client and 
     * transitions to the login screen.
     * @param event the event triggered by the logout button
     * @throws Exception if an error occurs during the process
     */
    @FXML
    void getBtnLogout(ActionEvent event) throws Exception  {
    	
        Message logoutMessage = new Message(BranchManagerController.branchManager.getId(), Commands.LogoutUser);
        ClientController.client.sendToServer(logoutMessage);
        
		((Node) event.getSource()).getScene().getWindow().hide();
		LoginScreenController newScreen = new LoginScreenController();
		newScreen.start(new Stage());
    	
    }

    /**
     * Handles the update client button action.
     * This method is triggered when the update client button is clicked.
     * It hides the current window and opens the update client screen, and
     * passes branch manager district
     * @param event the event triggered by the update client button click
     * @throws Exception if there is an error while opening the update client screen
     */
    @FXML
    public void getBtnUpdateClient(ActionEvent event) throws Exception {
    	UpdateClientController.setbranchManagerDistrict(branchManager.getDistrict());
		((Node) event.getSource()).getScene().getWindow().hide();
		UpdateClientController newScreen = new UpdateClientController();
		newScreen.start(new Stage());
    }

    @FXML
    void getBtnViewReports(ActionEvent event) {

    }

}
