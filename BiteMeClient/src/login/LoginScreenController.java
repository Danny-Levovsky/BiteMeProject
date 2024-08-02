package login;


import client.Client;
import client.ClientController;
import entites.Message;
import entites.User;
import enums.Commands;

import resturant.CertifiedEmployeeController;
import resturant.EmployeeController;
import customer.CustomerController;
import ceo.CEOController;
import branch_manager.BranchManagerController;


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





public class LoginScreenController {

    @FXML
    private Button btnDisconnect;

    @FXML
    public TextField txtUserName;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;
    
    @FXML
    private Button btnBack;

    @FXML
    private Label txtmsg;
    
    private String username, password; 
    
    
    /*
   ((Node) event.getSource()).getScene().getWindow().hide();
	CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
	newScreen.start(new Stage());
*/


   
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/LoginScreen.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("LoginScreen");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
	
	@FXML
	private void initialize() {
		// Initially disable the back button
	    btnBack.setDisable(true);
	    Client.loginController = this;  // Set the loginController instance here
	}
	
	
	public void getBtnDisconnect(ActionEvent event) throws Exception {
		//after disconnection the only thing user can do is back
		btnBack.setDisable(false); 
		btnLogin.setDisable(true); 
		btnDisconnect.setDisable(true);
		
		Message disconnectClient = new Message(null,Commands.ClientDisconnect);
		ClientController.client.sendToServer(disconnectClient);
	}
		
    @FXML
    void getBtnBack(ActionEvent event) {
    	 if (!btnBack.isDisable()) {
	        try {
	            FXMLLoader loader = new FXMLLoader();
	            loader.setLocation(getClass().getResource("/login/ConnectionScreen.fxml")); // Update the path to your FXML
	            Parent previousScreen = loader.load();
	            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            Scene scene = new Scene(previousScreen);
	            stage.setScene(scene);
	            stage.setTitle("BiteMeClient");
	            stage.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    	 }
    }
    
    
    public void loginMsg( String msg) {
    	txtmsg.setText(msg);
    }
    
    @FXML
    void getBtnLogin(ActionEvent event) throws Exception {
    	username =  txtUserName.getText();
    	password =  txtPassword.getText();
    	
    	if(username.isEmpty() || password.isEmpty()) {
    		loginMsg("username and password are required fields");
    		return;
    	}
    	
    	  // Send a message to the server to check the username and password
        User user = new User(username, password);
        Message checkCredentialsMessage = new Message(user, Commands.CheckUsername);
        ClientController.client.sendToServer(checkCredentialsMessage);
    }
    
    public void handleServerResponse(Message message, Stage currentStage) {
        if (message.getCmd() == Commands.CheckUsername) {
            Object response = message.getObj();
            if (response instanceof String) {
                String responseStr = (String) response;
                if (responseStr.equals("username not found")) {
                    loginMsg("username is not found");
                } else if (responseStr.equals("incorrect password")) {
                    loginMsg("password is incorrect");
                }
            } else if (response instanceof User) {
                User user = (User) response;
                if (user.getIsLoggedIn() == 1) {
                	loginMsg("user is already logged in");
                }
                else {
                	 openUserSpecificWindow(user, currentStage);

                }
            }
        }
    }
    
    private void openUserSpecificWindow(User user, Stage currentStage) {
        try {
            currentStage.hide();
            switch (user.getType()) {
                case "CEO":
                    CEOController ceoController = new CEOController();
                    ceoController.start(new Stage());
                    break;
                case "Customer":
                    CustomerController customerController = new CustomerController();
                    customerController.start(new Stage());
                    break;
                case "Branch Manager":
                    BranchManagerController branchManagerController = new BranchManagerController();
                    branchManagerController.start(new Stage());
                    break;
                case "Employee":
                    EmployeeController employeeController = new EmployeeController();
                    employeeController.start(new Stage());
                    break;
                case "Certified Employee":
                    CertifiedEmployeeController certifiedEmployeeController = new CertifiedEmployeeController();
                    certifiedEmployeeController.start(new Stage());
                    break;
                default:
                    loginMsg("Unknown user type");
                    currentStage.show(); // Show the login stage again if user type is unknown
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentStage.show(); // Show the login stage again in case of an error
        }
    }
    

}    