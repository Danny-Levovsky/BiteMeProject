package login;
import client.ClientController;
import entites.Message;
import enums.Commands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    void getBtnLogin(ActionEvent event) {

    }

    @FXML
    void getBtnDisconnect(ActionEvent event) {

    }
    
    
    /*
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/LoginScreen.fxml"));
    	loader.setController(this); // Set the controller
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("LoginScreen");
    	primaryStage.setScene(scene);
    	//RemoveTopBar(primaryStage,root);
    	primaryStage.show();
	}
	
	public void xBtn(ActionEvent event) throws Exception {
		Message disconnectClient = new Message(null,Commands.ClientDisconnect);
		ClientController.client.sendToServer(disconnectClient);
		//((Node)event.getSource()).getScene().getWindow().hide();
		//System.exit(0);
	}
	*/

}
