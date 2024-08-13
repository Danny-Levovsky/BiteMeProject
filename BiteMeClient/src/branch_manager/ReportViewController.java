package branch_manager;


import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ceo.CEOController;
import client.Client;
import client.ClientController;
import entites.Message;
import entites.User;
import enums.Commands;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



/**
 * Controller class for managing and displaying different types of reports in the branch manager's view.
 * This class handles the generation and display of income, order, and performance reports, 
 * as well as the scheduling of report generation at the end of each month.
 * 
 * @author yosra
 */
public class ReportViewController {

	@FXML
	private Button btnBack;

	@FXML
	private BarChart<String, Number> barChart; // Number can be either Integer or Float

	@FXML
	private CategoryAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private Label txtRestaurant;

	/**
     * The ID of the restaurant for which the report is generated.
     */
	private static int restaurantId;
	
	/**
     * The month and year for which the report is generated.
     */
	private static String monthYear;
	
	/**
     * The district of the branch manager.
     */
	private static String district;
	
	/**
     * The type of report to generate (e.g., income report, order report, performance report).
     */
	private static String reportType;
	
	/**
     * The name of the restaurant for which the report is generated.
     */
	private static String restaurantName;
	
	/**
     * The User object representing the user who is currently logged in.
     */

	private static User user;

	 /**
     * Sets the details required for generating the report.
     * 
     * @param id       the restaurant ID
     * @param month    the month and year for the report
     * @param dis      the district
     * @param report   the type of report
     * @param name     the name of the restaurant
     */
	public static void setDetails(int id, String month, String dis, String report, String name) {
		restaurantId = id;
		monthYear = month;
		district = dis;
		reportType = report;
		restaurantName = name;
	}
	
	/**
     * Sets the user information.
     * 
     * @param user1 the User object representing the user
     */
	public static void setUser(User user1) {
		user = user1;
	}

	 /**
     * Starts the ReportView window.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/branch_manager/ReportView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ReportViewWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
     * Initializes the controller and determines which type of report to generate 
     * based on the reportType variable.
     */
	@FXML
	private void initialize() {
		
		 Client.reportViewController = this;  // Set the  reportViwController instance here
		
		if (reportType.equals("income report")) {
			incomeReport();
		} else if (reportType.equals("order report")) {
			orderReport();
		} else if (reportType.equals("performance report")) {
			performanceReport();
		}
	}

	
	/*
	 * start of Timer
	 * Timer-related methods for scheduling report generation 
	 * we need to create an object of the class : ReportViewController scheduler =
	 * new ReportViewController(); scheduler.startScheduler();
	 */
	

	 /**
     * Scheduler service used to schedule periodic tasks for generating reports.
     * It uses a single thread executor to run tasks at fixed intervals, such as the end of each month.
     */
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	/**
     * Starts the scheduler to generate reports at the end of every month.
     */
	public void startScheduler() {
		// Calculate the initial delay until the end of the current month
		long initialDelay = calculateInitialDelay();

		// Schedule the task to run at the end of every month
		scheduler.scheduleAtFixedRate(this::timer, initialDelay, 30, TimeUnit.DAYS);
	}

	/**
     * Calculates the initial delay until the end of the current month.
     * 
     * @return the initial delay in milliseconds
     */
	private long calculateInitialDelay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate lastDayOfMonth = now.toLocalDate().withDayOfMonth(now.toLocalDate().lengthOfMonth());
		LocalDateTime lastMomentOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
		long initialDelay = Date.from(lastMomentOfMonth.atZone(ZoneId.systemDefault()).toInstant()).getTime()
				- System.currentTimeMillis();
		return initialDelay;
	}
	
	/**
     * Invokes the generation of income, order, and performance reports.
     */
	private void timer() {
		incomeReport();
		orderReport();
		performanceReport();
	}

	/* end of timer */


	/**
     * Sends a request to the server to generate an order report.
     */
	public void orderReport() {
		
		Message msg = new Message(new Object[]{district,restaurantId,monthYear}, Commands. OrderReport);
        ClientController.client.handleMessageFromClientControllers(msg); 
	}

	/**
     * Handles the server response for the order report by displaying the data on a bar chart.
     * 
     * @param orderReportData an array containing quantities of Salad, Main Course, Dessert, and Drink
     */
	public void handleServerResponseOrder(int[] orderReportData) {

	    txtRestaurant.setText(restaurantName);

	    // Clear existing data and reset axis categories
	    barChart.getData().clear();
	    xAxis.getCategories().clear();
	    
	    // Disable chart animation
	    barChart.setAnimated(false);
	    
	    // Set chart title
	    barChart.setTitle("Quantity by Dish Type");
	    
	    // Create a new series
	    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	    series1.setName("orders report " + monthYear);
	    
	    // Add data to the series
	    XYChart.Data<String, Number> saladData = new XYChart.Data<>("Salad", orderReportData[0]);
	    XYChart.Data<String, Number> mainCourseData = new XYChart.Data<>("Main Course", orderReportData[1]);
	    XYChart.Data<String, Number> dessertData = new XYChart.Data<>("Dessert", orderReportData[2]);
	    XYChart.Data<String, Number> drinkData = new XYChart.Data<>("Drink", orderReportData[3]);
	    
	    series1.getData().add(saladData);
	    series1.getData().add(mainCourseData);
	    series1.getData().add(dessertData);
	    series1.getData().add(drinkData);
	    
	    // Set axis labels
	    xAxis.setLabel("Dish Type");
	    yAxis.setLabel("Quantity");
	    
	    // Add the series to the chart
	    barChart.getData().add(series1);

	    // Add labels to display the values above each bar
	    for (XYChart.Data<String, Number> data : series1.getData()) {
	        // Use Platform.runLater to ensure that the label is added after the bar is rendered
	        Platform.runLater(() -> {
	            Node node = data.getNode();
	            if (node != null) {
	                StackPane stackPane = (StackPane) node;
	                Label label = new Label(data.getYValue().toString());
	                label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;"); // Bold font weight
	                
	                // Position the label above the bar
	                label.setTranslateY(-label.getHeight() - 10); // Move the label above the bar, adjust -10 as necessary
	                
	                stackPane.getChildren().add(label);
	                StackPane.setAlignment(label, Pos.TOP_CENTER);
	            }
	        });
	    }

	    // Ensure the chart is refreshed correctly
	    barChart.layout();  
	}

	 /**
     * Sends a request to the server to generate an income report.
     */
	public void incomeReport() {
		Commands command = Commands.getIncomeReport;
		Object[] incomeReportData = {restaurantId, monthYear, district};
		Message message = new Message(incomeReportData, command);
		ClientController.client.handleMessageFromClientControllers(message);	
	}

	/**
     * Handles the server response for the income report by displaying the data on a bar chart.
     * 
     * @param incomeReportResultData an array containing income data for each week of the month
     */
	public void handleServerResponseIncome(int[] incomeReportResultData) {
	    
	    txtRestaurant.setText(restaurantName);
	    
	    // Clear existing data and reset axis categories
	    barChart.getData().clear();
	    xAxis.getCategories().clear();
	    
	    // Disable chart animation
	    barChart.setAnimated(false);
	    
	    // Set chart title
	    barChart.setTitle("Total Income per Week");
	    
	    // Create a new series
	    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	    series1.setName("income report " + monthYear);
	    
	    // Add data to the series
	    String[] weeks = {"1", "2", "3", "4"};
	    for (int i = 0; i < 4; i++) {
	        XYChart.Data<String, Number> data = new XYChart.Data<>(weeks[i], incomeReportResultData[i]);
	        series1.getData().add(data);

	        // Add labels to display the values above each bar
	        Platform.runLater(() -> {
	            Node node = data.getNode();
	            if (node != null) {
	                StackPane stackPane = (StackPane) node;
	                Label label = new Label(data.getYValue().toString());
	                label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
	                
	                // Position the label at the top of the bar
	                label.setTranslateY(-label.getHeight() -10); // Adjust as necessary
	                
	                stackPane.getChildren().add(label);
	                StackPane.setAlignment(label, Pos.TOP_CENTER);
	            }
	        });
	    }
	    
	    xAxis.setLabel("Week");
	    yAxis.setLabel("NIS");
	    
	    // Add the series to the chart
	    barChart.getData().add(series1);
	    
	    // Ensure the chart is refreshed correctly
	    barChart.layout(); 
	}

	 /**
     * Sends a request to the server to generate a performance report.
     */
	public void performanceReport() {
		 Message msg = new Message(new Object[]{district, monthYear }, Commands.getPerformanceReport);
        ClientController.client.handleMessageFromClientControllers(msg); 
	}
	
	/**
     * Handles the server response for the performance report by displaying the data on a bar chart.
     * 
     * @param performanceReportResultData an array containing the percentage of delivery delays for each week
     */
	public void handleServerResponsePerformance(int[] performanceReportResultData) {
	    // Clear existing data and reset axis categories
	    barChart.getData().clear();
	    xAxis.getCategories().clear();

	    // Disable chart animation
	    barChart.setAnimated(false);

	    // Set chart title
	    barChart.setTitle("Percentage of Delivery Delays per Week");

	    // Create a new series
	    XYChart.Series<String, Number> series = new XYChart.Series<>();
	    series.setName("Performance Report " + monthYear);

	    // Add data to the series
	    String[] weeks = {"1", "2", "3", "4"};
	    for (int i = 0; i < 4; i++) {
	        XYChart.Data<String, Number> data = new XYChart.Data<>(weeks[i], performanceReportResultData[i]);
	        series.getData().add(data);

	        // Add labels to display the values above each bar
	        Platform.runLater(() -> {
	            Node node = data.getNode();
	            if (node != null) {
	                StackPane stackPane = (StackPane) node;
	                Label label = new Label(data.getYValue().toString());
	                label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
	                
	                // Position the label at the top of the bar
	                label.setTranslateY(-label.getHeight() -10); // Adjust as necessary
	                
	                stackPane.getChildren().add(label);
	                StackPane.setAlignment(label, Pos.TOP_CENTER);
	            }
	        });
	    }

	    // Set up the x-axis
	    xAxis.setLabel("Week");
	    xAxis.setCategories(FXCollections.observableArrayList(weeks));

	    // Set up the y-axis
	    yAxis.setLabel("%");
	    yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, null, null));

	    // Add the series to the chart
	    barChart.getData().add(series);
	    
	    // Ensure the chart is refreshed correctly
	    barChart.layout();   
	}

	/**
     * Handles the action for the back button. This method is triggered when the back button is clicked. 
     * It hides the current window and opens the appropriate screen based on the user type.
     * If the user is a "Branch Manager," it navigates to the Branch Manager screen.
     * If the user is a "CEO," it navigates to the CEO screen.
     * 
     * @param event the event triggered by the back button click
     * @throws Exception if there is an error while opening the new screen
     */
	@FXML
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		
		if(user.getType().equals("Branch Manager")) {
			BranchManagerController newScreen = new BranchManagerController();
			newScreen.start(new Stage());
		}
		if(user.getType().equals("CEO")) {
			CEOController newScreen = new CEOController();
			newScreen.start(new Stage());
		}
	}
}
