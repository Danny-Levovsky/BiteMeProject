package login;

import client.ClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectionScreenController {

    @FXML
    private Button btnConnect;

    @FXML
    private TextField ipAddressT;

    @FXML
    private TextField portT;

    @FXML
    private Button btnExit;
    
    

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ConnectionScreen.fxml"));
    	loader.setController(this); // Set the controller
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("BiteMeClient");
    	primaryStage.setScene(scene);
    	//RemoveTopBar(primaryStage,root);
    	primaryStage.show();
	}

    private String getIpAddress() {
		return ipAddressT.getText();
	}
    
    private Integer getPort() {
		return Integer.valueOf(portT.getText());
	}
    
	/**
	 * Handles the action event for the connect button.
	 * Initiates a connection to the server with the provided IP address and port.
	 * @param event The action event generated by clicking the connect button.
	 * @throws Exception Throws Exception if an error occurs during execution.
	 */
    @FXML
    void getBtnConnect(ActionEvent event) {
    	ClientController clientController = new ClientController(getIpAddress(), getPort());
		clientController.display("Connected");
		((Node)event.getSource()).getScene().getWindow().hide();
		LoginController newScreen = new LoginController();
		newScreen.start(new Stage());
	}
    
//	Example of how to override the x button to do your bidding
//	@Override
//	public void xBtn(MouseEvent event) {
//		System.out.println("damn");
//		System.exit(0);
//	}

    @FXML
    void getBtnExit(ActionEvent event) {

    }
  
}
