package ceo;



import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import branch_manager.ReportViewController;
import client.ClientController;
import entites.Message;
import entites.User;
import enums.Commands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import login.LoginScreenController;



/**
 * The CEOController class handles the actions and interactions for the CEO's
 * interface. This includes generating reports, managing view settings, and
 * handling user inputs within the CEO's view.
 * 
 * @author yosra
 */
public class CEOController {

	  @FXML
	    private Label txtCEOName;

	    @FXML
	    private Button btnLogout;
	    
	    @FXML
	    private ComboBox<String> districtComboBox;

	    @FXML
	    private ComboBox<String> monthComboBox;

	    @FXML
	    private ComboBox<String> reportComboBox;

	    @FXML
	    private ComboBox<String> restaurantComboBox;

	    @FXML
	    private Button btnViewReport;

	    @FXML
	    private TextArea txtError;
	    
	    @FXML
	    private ComboBox<String> restaurantComboBox1;

	    @FXML
	    private ComboBox<String> quarterComboBox1;

	    @FXML
	    private Button btnViewReport1;
	    
	    @FXML
	    private ComboBox<String> quarterComboBox2;

	    @FXML
	    private ComboBox<String> quarterComboBox3;

	    @FXML
	    private Button btnViewReport2;
	    
	    @FXML
	    private ComboBox<String> restaurantComboBox2;

	    @FXML
	    private ComboBox<String> restaurantComboBox3;

	    @FXML
	    private ComboBox<String> quarterComboBox4;

	    @FXML
	    private Button btnViewReport3;
	    
	    @FXML
	    private Label txtError1;
	    
	    private Map<String, Integer> restaurantMap = new HashMap<>();
	    
	    private static User ceo;

	    
	    /**
		 * Sets the CEO user.
		 * @param user the User object representing the CEO
		 */
	    public static void setCEO(User user) {
	        ceo = user;
	    }

	    /**
	     * Starts the CEO interface.
	     * @param primaryStage the primary stage for this application
	     * @throws Exception if an error occurs during loading the FXML
	     */
	    public void start(Stage primaryStage) throws Exception {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceo/CEO.fxml"));
	    	Parent root = loader.load();
	    	Scene scene = new Scene(root);
	    	primaryStage.setTitle("CEOWindow");
	    	primaryStage.setScene(scene);
	    	primaryStage.show();
		}
	    
	    /**
	     * Initializes the controller class. This method is automatically called after
	     * the FXML file has been loaded. It populates the ComboBoxes with month/year
	     * strings, report options, district options, and restaurant names. It also updates the CEO's name on the UI, 
	     * and configures the initial states for various UI components.
	     */
	    @FXML
	    private void initialize() { 	
	        //update name
	        if(ceo != null) {
	        	txtCEOName.setText(ceo.getFirstName() + " " + ceo.getLastName());
	        }
	        
	     // Populate the ComboBox with month/year strings
			for (int i = 1; i <= 12; i++) {
				String monthYear = i + "/2024";
				monthComboBox.getItems().add(monthYear);
			}
			
			// Initialize the ComboBox with report options
			reportComboBox.getItems().addAll("income report", "order report", "performance report");
			
			// Initialize the ComboBox with district options
			districtComboBox.getItems().addAll("north", "center", "south");
			
			// Initialize the ComboBox with restaurant options and map with corresponding
			// numbers
			restaurantMap.put("The Savory Spoon", 1);
			restaurantMap.put("Bistro Belle Vie", 2);
			restaurantMap.put("Harvest Moon Cafe", 3);
			restaurantMap.put("Gourmet Garden", 4);
			restaurantMap.put("Urban Palate", 5);

			// Add restaurant names to the ComboBox
			restaurantComboBox.getItems().addAll(restaurantMap.keySet());
			restaurantComboBox1.getItems().addAll(restaurantMap.keySet());
			restaurantComboBox2.getItems().addAll(restaurantMap.keySet());
			restaurantComboBox3.getItems().addAll(restaurantMap.keySet());
			
			String[] quarter = {"Q1", "Q2", "Q3", "Q4"};
			
			for(int i = 0; i < 4; i++) {
				quarterComboBox1.getItems().addAll(quarter[i]);
				quarterComboBox2.getItems().addAll(quarter[i]);
				quarterComboBox3.getItems().addAll(quarter[i]);
				quarterComboBox4.getItems().addAll(quarter[i]);
			}

			// Disable restaurant ComboBox and "View Reports" button initially
			restaurantComboBox.setDisable(true);
			btnViewReport.setDisable(true);
			txtError.setVisible(false);
			txtError1.setVisible(false);
	    }
	    
	    /**
	     * Handles the action for the "View Report" button. It gathers the selected 
	     * restaurant, month, report type, and district, and then opens the Report View screen.
	     * @param event the event triggered by the "View Report" button click
	     * @throws Exception if there is an error while opening the Report View screen
	     */
	    @FXML
	    public void getBtnViewReport(ActionEvent event) throws Exception {
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

			// Get district 
			String district = districtComboBox.getSelectionModel().getSelectedItem();

			// Call the setDetails method of ReportViewController with the gathered data
			ReportViewController.setDetails(restaurantNumber, selectedMonthYear, district, selectedReport,
					selectedRestaurant);
			ReportViewController.setUser(ceo);

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
	     * @param event the event triggered by selecting a month/year
	     */
	    @FXML
	    public void getMonthComboBox(ActionEvent event) {
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
					btnViewReport.setDisable(false); 
					txtError.setVisible(false); 
				} else {
					btnViewReport.setDisable(true); 
					txtError.setVisible(true);
				}
			} catch (DateTimeParseException e) {
				btnViewReport.setDisable(true); 
				txtError.setVisible(false);
			}
	    }

	    /**
	     * Handles the action when a report type is selected from the ComboBox. This method
	     * enables or disables the restaurant ComboBox based on the selected report type.
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
	    
	    /**
	     * Handles the action for the "View Report" button associated with the first quarter ComboBox. 
	     * This method gathers the selected restaurant and quarter, and then opens the Quarter Report View 1 screen.
	     * @param event the event triggered by the "View Report" button click
	     * @throws Exception if there is an error while opening the Quarter Report View 1 screen
	     */
	    @FXML
	    public void getBtnViewReport1(ActionEvent event) throws Exception {
	    	
	    	String selectedRestaurant = restaurantComboBox1.getSelectionModel().getSelectedItem();
	    	Integer restaurantNumber = restaurantMap.get(selectedRestaurant);
	    	String quarter = quarterComboBox1.getSelectionModel().getSelectedItem();
	    	
	    	QuarterReportView1.setDetails(restaurantNumber, selectedRestaurant, quarter);	
	    	
	    	((Node) event.getSource()).getScene().getWindow().hide();
			QuarterReportView1 newScreen = new QuarterReportView1 ();
			newScreen.start(new Stage());
	    }
	    
	    /**
	     * Handles the action for checking the selected quarter from the ComboBox. 
	     * This method checks if the selected quarter has passed or not. If the quarter hasn't passed, 
	     * it disables the corresponding "View Report" button and shows an error message.
	     * @param event the event triggered by selecting a quarter
	     */
	    @FXML
	    void checkQuarter(ActionEvent event) {
	    	 // Determine which ComboBox triggered the event
	        ComboBox<String> sourceComboBox = (ComboBox<String>) event.getSource();
	        String selectedQuarter = sourceComboBox.getValue();
	        LocalDate currentDate = LocalDate.now();
	        LocalDate endDate = null;

	         switch (selectedQuarter) {
	             case "Q1":
	                 endDate = LocalDate.of(Year.now().getValue(), Month.MARCH, 31);
	                 break;
	             case "Q2":
	                 endDate = LocalDate.of(Year.now().getValue(), Month.JUNE, 30);
	                 break;
	             case "Q3":
	                 endDate = LocalDate.of(Year.now().getValue(), Month.SEPTEMBER, 30);
	                 break;
	             case "Q4":
	                 endDate = LocalDate.of(Year.now().getValue(), Month.DECEMBER, 31);
	                 break;
	         }

	      // For quarterComboBox1
	         if (sourceComboBox.equals(quarterComboBox1)) {
	             if (currentDate.isBefore(endDate)) {
	                 txtError1.setVisible(true);
	                 btnViewReport1.setDisable(true);
	             } else {
	                 txtError1.setVisible(false);
	                 btnViewReport1.setDisable(false);
	             }
	         } 
	         // For quarterComboBox2
	         else if (sourceComboBox.equals(quarterComboBox2)) {
	             if (currentDate.isBefore(endDate)) {
	                 txtError1.setVisible(true);
	                 btnViewReport2.setDisable(true);
	                 quarterComboBox3.setDisable(true); // Disable quarterComboBox3
	             } else {
	                 txtError1.setVisible(false);
	                 btnViewReport2.setDisable(false);
	                 quarterComboBox3.setDisable(false); // Enable quarterComboBox3
	             }
	         }
	         // For quarterComboBox3
	         else if (sourceComboBox.equals(quarterComboBox3)) {
	             if (currentDate.isBefore(endDate)) {
	                 txtError1.setVisible(true);
	                 btnViewReport2.setDisable(true);
	             } else {
	                 txtError1.setVisible(false);
	                 btnViewReport2.setDisable(false);
	             }
	         } 
	         // For quarterComboBox4
	         else if (sourceComboBox.equals(quarterComboBox4)) {
	             if (currentDate.isBefore(endDate)) {
	                 txtError1.setVisible(true);
	                 btnViewReport3.setDisable(true);
	             } else {
	                 txtError1.setVisible(false);
	                 btnViewReport3.setDisable(false);
	             }
	         } 
	    }

	    /**
	     * Handles the action for the "View Report" button associated with the second quarter ComboBox. 
	     * This method gathers the selected restaurant and quarters, and then opens the Quarter Report View 2 screen.
	     * @param event the event triggered by the "View Report" button click
	     * @throws Exception if there is an error while opening the Quarter Report View 2 screen
	     */
	    @FXML
	    void getBtnViewReport2(ActionEvent event) throws Exception {
	    	String selectedRestaurant = restaurantComboBox1.getSelectionModel().getSelectedItem();
	    	Integer restaurantNumber = restaurantMap.get(selectedRestaurant);
	    	String quarter1 = quarterComboBox2.getSelectionModel().getSelectedItem();
	    	String quarter2 = quarterComboBox3.getSelectionModel().getSelectedItem();
	    	
	    	QuarterReportView2.setDetails(new int[]{restaurantNumber, 0},new String[]{selectedRestaurant, null},new String[]{quarter1, quarter2});
	    	
	    	((Node) event.getSource()).getScene().getWindow().hide();
			QuarterReportView2 newScreen = new QuarterReportView2 ();
			newScreen.start(new Stage());
	    }
	    
	    /**
	     * Handles the action for the "View Report" button associated with the third quarter ComboBox. 
	     * This method gathers the selected restaurants and quarter, and then opens the Quarter Report View 2 screen.
	     * @param event the event triggered by the "View Report" button click
	     * @throws Exception if there is an error while opening the Quarter Report View 2 screen
	     */
	    @FXML
	    void getBtnViewReport3(ActionEvent event) throws Exception {
	    	String selectedRestaurant1 = restaurantComboBox2.getSelectionModel().getSelectedItem();
	    	String selectedRestaurant2 = restaurantComboBox3.getSelectionModel().getSelectedItem();
	    	Integer restaurantNumber1 = restaurantMap.get(selectedRestaurant1);
	    	Integer restaurantNumber2 = restaurantMap.get(selectedRestaurant2);
	    	String quarter = quarterComboBox4.getSelectionModel().getSelectedItem();
	    	
	    	QuarterReportView2.setDetails(new int[]{restaurantNumber1, restaurantNumber2},new String[]{selectedRestaurant1, selectedRestaurant2},new String[]{quarter, null});
	    	((Node) event.getSource()).getScene().getWindow().hide();
			QuarterReportView2 newScreen = new QuarterReportView2 ();
			newScreen.start(new Stage());

	    }

	    /**
	     * Handles the logout button action. Sends a logout message to the server
	     * through the client and transitions to the login screen.
	     * @param event the event triggered by the logout button
	     * @throws Exception if an error occurs during the process
	     */
	    @FXML
	    public void getBtnLogout(ActionEvent event) throws Exception {
	    	Message logoutMessage = new Message( CEOController.ceo.getId(), Commands.LogoutUser);
			ClientController.client.sendToServer(logoutMessage);

			((Node) event.getSource()).getScene().getWindow().hide();
			LoginScreenController newScreen = new LoginScreenController();
			newScreen.start(new Stage());
	    }	
}
