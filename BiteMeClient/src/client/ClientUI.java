package client;

import javafx.application.Application;
import javafx.stage.Stage;
import login.ConnectionScreenController;


/**
 * ClientUI serves as the entry point for the JavaFX application. 
 * It initializes and starts the application by displaying the connection screen.
 * 
 * @author yosra
 */
public class ClientUI extends Application {

	/**
	 * The main method that launches the JavaFX application.
	 * 
	 * @param args the command-line arguments
	 * @throws Exception if an error occurs during application launch
	 */
	public static void main(String args[]) throws Exception {
		launch(args); // launch application.
	} // end main

	/**
	 * Starts the JavaFX application by setting up the primary stage.
	 * It initializes and displays the connection screen.
	 * 
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if an error occurs during the loading of the connection screen
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		ConnectionScreenController mainScreen = new ConnectionScreenController(); // create MainScreen and start it.
		mainScreen.start(primaryStage);

	}
}
