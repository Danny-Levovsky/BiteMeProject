package branch_manager;

import entites.User;
import enums.Commands;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import client.ClientController;
import entites.Message;
import login.LoginScreenController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controller class for managing the branch manager's user interface. 
 * This class handles user interactions, manages UI components, 
 * and controls the flow of data between the UI and the backend services.
 * @author yosra
 */
public class BranchManagerController {

	@FXML
	private Label txtBranchManagerName;

	@FXML
	private Button btnLogout;

	@FXML
	private Button BtnUpdateClient;

	@FXML
	private Button BtnViewReports;

	@FXML
	private ComboBox<String> monthComboBox;

	@FXML
	private ComboBox<String> reportComboBox;

	@FXML
	private ComboBox<String> restaurantComboBox;

    @FXML
    private TextArea txtError;
    
    /**
     * Map that links restaurant names to their corresponding numeric IDs.
     */
	private Map<String, Integer> restaurantMap = new HashMap<>();

	/**
     * The User object representing the branch manager.
     */
	private static User branchManager;

	/**
     * Sets the branch manager user.
     * 
     * @param user the User object representing the branch manager
     */
	public static void setbranchManager(User user) {
		branchManager = user;
	}

	 /**
     * Starts the branch manager interface.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/branch_manager/BranchManager.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("BranchManagerWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	 /**
     * Initializes the controller class. This method is automatically called after
     * the FXML file has been loaded. It populates the ComboBoxes with month/year
     * strings, report options, and restaurant names. It also updates the branch
     * manager's name on the UI, disables the "View Reports" button by default, and
     * hides the error message.
     */
	@FXML
	private void initialize() {
		// Populate the ComboBox with month/year strings
		for (int i = 1; i <= 12; i++) {
			String monthYear = i + "/2024";
			monthComboBox.getItems().add(monthYear);
		}

		// Initialize the ComboBox with report options
		reportComboBox.getItems().addAll("income report", "order report", "performance report");

		// Initialize the ComboBox with restaurant options and map with corresponding
		// numbers
		restaurantMap.put("The Savory Spoon", 1);
		restaurantMap.put("Bistro Belle Vie", 2);
		restaurantMap.put("Harvest Moon Cafe", 3);
		restaurantMap.put("Gourmet Garden", 4);
		restaurantMap.put("Urban Palate", 5);

		// Add restaurant names to the ComboBox
		restaurantComboBox.getItems().addAll(restaurantMap.keySet());

		// update name
		if (branchManager != null) {
			txtBranchManagerName.setText(branchManager.getFirstName() + " " + branchManager.getLastName());
		}
		 // Disable restaurant ComboBox and "View Reports" button initially
		restaurantComboBox.setDisable(true);
		BtnViewReports.setDisable(true);
		txtError.setVisible(false);
	}

	 /**
     * Handles the logout button action. Sends a logout message to the server
     * through the client, hides the current window, and transitions to the login screen.
     * 
     * @param event the event triggered by the logout button
     * @throws Exception if an error occurs during the process
     */
	@FXML
	public void getBtnLogout(ActionEvent event) throws Exception {

		Message logoutMessage = new Message(BranchManagerController.branchManager.getId(), Commands.LogoutUser);
		ClientController.client.sendToServer(logoutMessage);

		((Node) event.getSource()).getScene().getWindow().hide();
		LoginScreenController newScreen = new LoginScreenController();
		newScreen.start(new Stage());

	}

	 /**
     * Handles the update client button action. This method is triggered when the
     * update client button is clicked. It hides the current window and opens the
     * update client screen, passing the branch manager's district.
     * 
     * @param event the event triggered by the update client button click
     * @throws Exception if there is an error while opening the update client screen
     */
	@FXML
	public void getBtnUpdateClient(ActionEvent event) throws Exception {
		UpdateClientController.setbranchManagerDistrict(branchManager.getDistrict());
		((Node) event.getSource()).getScene().getWindow().hide();
		UpdateClientController newScreen = new UpdateClientController();
		newScreen.start(new Stage());
	}

	 /**
     * Handles the view reports button action. This method is triggered when the
     * view reports button is clicked. It gathers the selected restaurant, month,
     * report type, and the branch manager's district, then opens the report view
     * screen.
     * 
     * @param event the event triggered by the view reports button click
     * @throws Exception if there is an error while opening the report view screen
     */
	@FXML
	public void getBtnViewReports(ActionEvent event) throws Exception {

		// Get selected restaurant name and corresponding number
		String selectedRestaurant = restaurantComboBox.getSelectionModel().getSelectedItem();
		
		// Check if no selection was made and set selectedRestaurant to null
		if (selectedRestaurant == null) {
		    selectedRestaurant = null;
		}
		
		Integer restaurantNumber = 0;
		if(selectedRestaurant != null) {
			restaurantNumber = restaurantMap.get(selectedRestaurant);
		}

		// Get selected month/year
		String selectedMonthYear = monthComboBox.getSelectionModel().getSelectedItem();

		// Get selected report type
		String selectedReport = reportComboBox.getSelectionModel().getSelectedItem();

		// Get district of the branch manager
		String district = branchManager.getDistrict();

		// Call the setDetails method of ReportViewController with the gathered data
		ReportViewController.setDetails(restaurantNumber, selectedMonthYear, district, selectedReport,
				selectedRestaurant);
		ReportViewController.setUser(branchManager);

		((Node) event.getSource()).getScene().getWindow().hide();
		ReportViewController newScreen = new ReportViewController();
		newScreen.start(new Stage());
	}

	 /**
     * Handles the action when a month is selected from the ComboBox. This method
     * checks if the selected month/year is before or the same as the current
     * month/year. If it is, the "View Reports" button is enabled, and any error
     * message is hidden. Otherwise, the button is disabled, and an error message is
     * displayed.
     * 
     * @param event the event triggered by selecting a month/year
     */
	@FXML
	public void getMonth(ActionEvent event) {

		// Get the selected item from the ComboBox
		String selectedMonthYear = monthComboBox.getSelectionModel().getSelectedItem();

		try {
			// Parse the selected month and year directly without modifying the input string
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/yyyy");
			YearMonth selectedDate = YearMonth.parse(selectedMonthYear, formatter);

			// Get the current month and year
			YearMonth currentDate = YearMonth.now();
			
			// Get the last day of the current month
	        LocalDate lastDayOfCurrentMonth = currentDate.atEndOfMonth();
	        LocalDate today = LocalDate.now();


			// Check if the selected date is before or the same as the current date
			if (selectedDate.isBefore(currentDate) || (selectedDate.equals(currentDate) && today.equals(lastDayOfCurrentMonth))) {
				BtnViewReports.setDisable(false); 
				txtError.setVisible(false); 
			} else {
				BtnViewReports.setDisable(true); 
				txtError.setVisible(true);
			}
		} catch (DateTimeParseException e) {
			BtnViewReports.setDisable(true); 
			txtError.setVisible(false);
		}
	}
	
	/**
     * Handles the action when a report type is selected from the ComboBox. Enables
     * the restaurant ComboBox if the selected report type is not "performance report".
     * 
     * @param event the event triggered by selecting a report type
     */
    @FXML
    public void getReportComboBox(ActionEvent event) {
    	// Get selected report type
    	String selectedReport = reportComboBox.getSelectionModel().getSelectedItem();
    	if (selectedReport.equals("performance report")) {
    		restaurantComboBox.setDisable(true);
    	}
    	else {
    		restaurantComboBox.setDisable(false);
    	}
    }
}
