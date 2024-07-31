package login;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import resturant.CertifiedEmployeeController;

public class LoginScreenController {

    @FXML
    private Button btnDisconnect;

    @FXML
    private TextField txtUserName;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;
    
    
    @FXML
    void getBtnLogin(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
    	newScreen.start(new Stage());
    }
 
    
    
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/LoginScreen.fxml"));
    	//loader.setController(this); // Set the controller = loads in fxml file
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("LoginScreen");
    	primaryStage.setScene(scene);
    	//RemoveTopBar(primaryStage,root);
    	primaryStage.show();
	}
	
	public void getBtnDisconnect(ActionEvent event) throws Exception {
		Message disconnectClient = new Message(null,Commands.ClientDisconnect);
		ClientController.client.sendToServer(disconnectClient);
		//((Node)event.getSource()).getScene().getWindow().hide();
		//System.exit(0);
	}
	

}
