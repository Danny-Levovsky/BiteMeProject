package branch_manager;


import client.Client;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;




/**
 * The UpdateClientController class handles the update client UI actions and interactions.
 * This includes initializing the update client window and handling button actions.
 * @author yosra
 */
public class UpdateClientController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnActive;

    @FXML
    private TextField txtID;

    @FXML
    private Label txtMsg;
    
    private int id; 
    private static String district;
    
    /**
     * Sets the district for the branch manager.
     * @param dis the district name
     */
    public static void setbranchManagerDistrict(String dis) {
       district = dis;
    }

    /**
     * Starts and displays the update client window.
     * @param primaryStage the primary stage for this application
     * @throws Exception if there is an error during the loading of the FXML file
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/branch_manager/UpdateClient.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("UpdateClientWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
	/**
     * Initializes the controller class. This method is automatically called after the
     * FXML file has been loaded.
     */
	@FXML
	private void initialize() {

	    Client.updateClientController = this;  // Set the updateClientcontroller instance here
	}
    
	/**
     * Displays a message on the UI.
     * @param msg the message to display
     */
    public void appearingMsg( String msg) {
            txtMsg.setText(msg);
    }
    
    /**
     * Handles the action for the activate button.
     * This method is triggered when the activate button is clicked.
     * It sends a request to the server through client to update the client's status.
     * @param event the event triggered by the activate button click
     * @throws Exception if there is an error while sending the request to the server
     */
    @FXML
    public void getActiveBtn(ActionEvent event) throws Exception {
    	try {
    	    id = Integer.parseInt(txtID.getText());
    	 // Send request to server
    	    Message msg = new Message(new Object[]{id, district}, Commands. UpdateStatus);
            ClientController.client.handleMessageFromClientControllers(msg); 
    	} catch (NumberFormatException e) {
    		appearingMsg("you can only insert an integer");
    	}
    }
    
    /**
     * Handles the response from the server.
     * @param message the message received from the server
     */
    public void handleServerResponse(Message message) {
    	 if (message.getCmd() == Commands. UpdateStatus) {
    		 Object response = message.getObj();
    		 if (response instanceof String) {
                 String responseStr = (String) response;
                 if (responseStr.equals("user not found")) {
                	 appearingMsg("user not found"); 	 
                 } 
                 else if (responseStr.equals("no permission")) {
                     appearingMsg("you have no permission to update user");
                 }  
                 else if (responseStr.equals("user already active")) {
                     appearingMsg("user already active");
                 } 
                 else if (responseStr.equals("status updated successfully")) {
                	 appearingMsg("status updated successfully");
                 }
                     
    		 }
    	 }
    }
    
    /**
     * Handles the action for the back button.
     * This method is triggered when the back button is clicked.
     * It hides the current window and opens the branch manager screen.
     * @param event the event triggered by the back button click
     * @throws Exception if there is an error while opening the branch manager screen
     */
    @FXML
    public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		BranchManagerController newScreen = new BranchManagerController();
		newScreen.start(new Stage());
    }

}


