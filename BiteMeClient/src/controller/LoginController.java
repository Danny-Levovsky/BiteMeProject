package controller;

import client.ClientController;
import enums.Commands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import entites.Message;


public class LoginController{
	
	@FXML
    private Button exitBtn = null;


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
}

