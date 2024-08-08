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




/**
 * Controller class for the login screen of the application.
 * Manages user interactions with the login screen and communicates with the server through client to authenticate users.
 * @author yosra
 */
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
    

    /**
     * Starts the JavaFX application by setting up the primary stage with the login screen. 
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     * @throws Exception if the FXML file cannot be loaded
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/LoginScreen.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("LoginScreen");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
	/**
     * Initializes the login screen controller.
     * Disables the back button initially and sets the login controller instance.
     */
	@FXML
	private void initialize() {
		// Initially disable the back button
	    btnBack.setDisable(true);
	    Client.loginController = this;  // Set the loginController instance here
	}
	
	/**
     * Handles the action event for the disconnect button.
     * Disables the login and disconnect buttons and sends a disconnect message to the server.
     * @param event the action event triggered by the disconnect button
     * @throws Exception if there is an error sending the message to the server
     */
    @FXML
	public void getBtnDisconnect(ActionEvent event) throws Exception {
		//after disconnection the only thing user can do is back
		btnBack.setDisable(false); 
		btnLogin.setDisable(true); 
		btnDisconnect.setDisable(true);
		
		Message disconnectClient = new Message(null,Commands.ClientDisconnect);
		ClientController.client.sendToServer(disconnectClient);
	}
	
    /**
     * Handles the action event for the back button.
     * Loads the connection screen FXML and sets it as the last screen
     * @param event the action event triggered by the back button
     */
    @FXML
    public void getBtnBack(ActionEvent event) throws Exception{
    	 if (!btnBack.isDisable()) {
    		 ((Node) event.getSource()).getScene().getWindow().hide();
    		 ConnectionScreenController newScreen = new ConnectionScreenController();
    		 newScreen.start(new Stage());
    	 }
    }
    
    /**
     * Sets the login message to be displayed on the login screen.
     * @param msg the login message to be displayed
     */
    public void loginMsg( String msg) {
    	txtmsg.setText(msg);
    }
    
    /**
    * Handles the action event for the login button.
    * Validates the input fields and sends a login request to the server through client.
    * @param event the action event triggered by the login button
    * @throws Exception if there is an error sending the message to the server
    */
    @FXML
    public void getBtnLogin(ActionEvent event) throws Exception {
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
    
    /**
     * Handles the server response for the login request.
     * Processes the server response and performs the appropriate action based on the response.
     * @param message the message received from the server
     * @param currentStage the current stage of the application
     * @throws Exception if there is an error handling the server response
     */
    public void handleServerResponse(Message message, Stage currentStage) throws Exception {
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
                	Message updateLoginStatusMessage = new Message(user.getId(), Commands.UpdateLoginStatus);
                    ClientController.client.sendToServer(updateLoginStatusMessage);
                	openUserSpecificWindow(user, currentStage);

                }
            }
        }
    }
    
    /**
     * Opens a specific window based on the user type.
     * @param user the user object containing user details
     * @param currentStage the current stage of the application
     */
    private void openUserSpecificWindow(User user, Stage currentStage) {
        try {
            currentStage.hide();
            switch (user.getType()) {
                case "CEO":
                	CEOController.setCEO(user);
                    CEOController ceoController = new CEOController();
                    ceoController.start(new Stage());
                    break;
                case "Customer":
                	CustomerController.setCustomer(user);
                    CustomerController customerController = new CustomerController();
                    customerController.start(new Stage());
                    break;
                case "Branch Manager":
                    BranchManagerController.setbranchManager(user);
                    BranchManagerController branchManagerController = new BranchManagerController();
                    branchManagerController.start(new Stage());
                    break;
                case "Employee":
                	EmployeeController.setEmployee(user);
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
