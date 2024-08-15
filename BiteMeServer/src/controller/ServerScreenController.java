package controller;

import JDBC.DbController;
import JDBC.SqlConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.InetAddress;
import entites.ClientDetails;
import server.NotifyThread;
import server.ServerUI;


/**
 * The ServerScreenController class manages the server's GUI, allowing the user to start and stop the server, 
 * manage client connections, and handle database connectivity. It also provides functionality to import data 
 * and update the client table view.
 * 
 * @author yosra
 */
public class ServerScreenController {

	/**
     * The thread responsible for sending notifications to clients.
     */
	NotifyThread alertThread;

	@FXML
	private TextField ipAddressT;
	@FXML
	private TextField portT;
	@FXML
	private TextField dbNameT;
	@FXML
	private TextField dbUsernameT;
	@FXML
	private TextField dbPasswordT;
	@FXML
	private Button startServer = null;
	@FXML
	private Button stopServer = null;
	@FXML
	private Button imprt;

	@FXML
	private TableView<ClientDetails> tableView;

	@FXML
	private TableColumn ipT;
	@FXML
	private TableColumn hostT;
	@FXML
	private TableColumn statusT;

	// TODO: Initilize Client Table.
	// TODO: Make The X button Functional.
	// TODO: Make Stop Server Button.

	/**
     * Retrieves the port number entered by the user.
     * 
     * @return the port number as a String
     */
	private String getPort() {
		return portT.getText();
	}

	/**
     * Retrieves the database name entered by the user.
     * 
     * @return the database name as a String
     */
	private String getDbName() {
		return dbNameT.getText();
	}

	/**
     * Retrieves the database username entered by the user.
     * 
     * @return the database username as a String
     */
	private String getDbUsername() {
		return dbUsernameT.getText();
	}

	/**
     * Retrieves the database password entered by the user.
     * 
     * @return the database password as a String
     */
	private String getDbPassword() {
		return dbPasswordT.getText();
	}

	/**
     * Loads client details into the TableView.
     * 
     * @param clientDetail the ClientDetails object containing client information
     */
	public void loadTable(ClientDetails clientDetail) {// Client : {String IP, String Host, String Status}

		tableView.getItems().add(clientDetail);

	}

	/**
     * Updates the TableView by removing the specified client details.
     * 
     * @param clientDetail the ClientDetails object to be removed
     */
	public void updateTable(ClientDetails clientDetail) {

		boolean returnVal = tableView.getItems().remove(clientDetail);

	}

	/**
     * Handles the stop server button action. Stops the server, stops the notification thread, 
     * and updates the UI accordingly.
     * 
     * @param event the ActionEvent triggered by the stop server button
     */
	public void stopServerBtn(ActionEvent event) {
		// Stop the NotifyThread gracefully
		alertThread.stopThread(); // Assuming alertThread is accessible here

		ServerUI.disconnect();

		startServer.setDisable(false);
		stopServer.setDisable(true);
		disableDataInput(false);
		imprt.setDisable(true);

	}

	/**
     * Handles the start server button action. Starts the server, initializes the database connection, 
     * and starts the notification thread.
     * 
     * @param event the ActionEvent triggered by the start server button
     */
	public void startServerBtn(ActionEvent event) {

		if (!ServerUI.isServerRunning()) {
			// The server is not running, so start it
			if (ServerUI.runServer(this.getPort())) {
				SqlConnection sqlconn = new SqlConnection(getDbName(), getDbUsername(), getDbPassword());
				DbController dbconn = new DbController(sqlconn.connectToDB());
				ServerUI.sv.setDbController(dbconn);
				ServerUI.sv.setServerScreenController(this);
				disableDataInput(true);
				startServer.setDisable(true);
				imprt.setDisable(false);
				stopServer.setDisable(false);

				// setup new thread
				alertThread = new NotifyThread();
				Thread thread = new Thread(alertThread); // Create a new thread with NotifyThread as its target
				thread.start(); // Start the new thread

				// try { Thread.sleep(10000); } catch (InterruptedException e) {
				// e.printStackTrace(); }
				// loadTable();

			} else {
				// Handle the case where the server failed to start
				System.out.println("Failed to start the server.");
			}
		} else {
			// The server is already running, handle accordingly
			System.out.println("The server is already running.");
		}
	}

	/**
     * Handles the import data button action. Imports external data into the server's database.
     * 
     * @param event the ActionEvent triggered by the import data button
     */
	public void importBtn(ActionEvent event) {
		ServerUI.sv.dbController.importExternalData();
		imprt.setDisable(true);

	}

	/**
     * Disables or enables the data input fields based on the provided condition.
     * 
     * @param condition if true, disables the input fields; if false, enables them
     */
	void disableDataInput(boolean Condition) {
		ipAddressT.setDisable(Condition);
		portT.setDisable(Condition);
		dbNameT.setDisable(Condition);
		dbUsernameT.setDisable(Condition);
		dbPasswordT.setDisable(Condition);
	}

	/**
    * Starts the server GUI by loading the FXML file and displaying the scene.
    * 
    * @param primaryStage the primary stage for this application
    * @throws Exception if an error occurs during loading the FXML file
    */
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/serverScreen.fxml"));
		System.out.println("starting server gui");
		loader.setController(this); // Set the controller
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		try {
			String ipAddress = InetAddress.getLocalHost().getHostAddress();
			ipAddressT.setText(ipAddress);
		} catch (Exception e) {
			ipAddressT.setText("Unable to get IP address");
		}
		ipT.setCellValueFactory(new PropertyValueFactory<>("ip"));
		hostT.setCellValueFactory(new PropertyValueFactory<>("hostName"));
		statusT.setCellValueFactory(new PropertyValueFactory<>("status"));
		stopServer.setDisable(true);

	}

}
