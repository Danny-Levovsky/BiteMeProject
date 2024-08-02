package login;

import javafx.scene.control.Label;
import java.net.ConnectException;

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
    
    @FXML
    private Label errorIPLabel;
    
    
    public void initialize() {

        // Disable the Connect button initially
        btnConnect.setDisable(true);

        // Add listeners to the TextFields to enable/disable the Connect button
        ipAddressT.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
        portT.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
    }

    private void validateInput() {
        String ip = ipAddressT.getText();
        String port = portT.getText();
        boolean isIpValid = ip != null && !ip.trim().isEmpty();
        boolean isPortValid = port != null && !port.trim().isEmpty();
        
        // Enable the Connect button only if both fields are non-empty
        btnConnect.setDisable(!(isIpValid && isPortValid));
    }
    
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/ConnectionScreen.fxml"));
    	//loader.setController(this); // Set the controller = is set in FXML file
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
        try {
            ClientController clientController = new ClientController(getIpAddress(), getPort());
            clientController.display("Connected");

            // Move to the login screen only if the connection is successful
            ((Node) event.getSource()).getScene().getWindow().hide();
            LoginScreenController newScreen = new LoginScreenController();
            newScreen.start(new Stage());

        } catch (ConnectException e) {
        	errorIPLabel.setVisible(true);
        	System.out.println("Unable to connect to the IP address");
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
    
    
//	Example of how to override the x button to do your bidding
//	@Override
//	public void xBtn(MouseEvent event) {
//		System.out.println("damn");
//		System.exit(0);
//	}

    @FXML
    void getBtnExit(ActionEvent event) {
	    System.exit(1); 

    }
  
}
