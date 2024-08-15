package ceo;

import client.Client;
import client.ClientController;
import entites.Message;
import enums.Commands;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller class for viewing the quarterly reports for two different quarters or restaurants
 * in the CEO interface. This class handles the display of order distribution and total income 
 * per week for the selected restaurants and quarters.
 * 
 * @author yosra
 */
public class QuarterReportView2 {

	@FXML
	private Button btnBack;

	@FXML
	private BarChart<String, Number> barChart1;

	@FXML
	private CategoryAxis xAxis1;

	@FXML
	private NumberAxis yAxis1;

	@FXML
	private Label txtRestaurant;

	@FXML
	private Label txtRestaurant2;

	@FXML
	private Label txtTotalIncome;

	@FXML
	private Label txtTotalIncome1;

	@FXML
	private BarChart<String, Number> barChart2;

	@FXML
	private CategoryAxis xAxis2;

	@FXML
	private NumberAxis yAxis2;

	@FXML
	private BarChart<String, Number> barChart3;

	@FXML
	private CategoryAxis xAxis3;

	@FXML
	private NumberAxis yAxis3;

	@FXML
	private BarChart<String, Number> barChart4;

	@FXML
	private CategoryAxis xAxis4;

	@FXML
	private NumberAxis yAxis4;

	/**
     * The ID of the first restaurant for which the report is generated.
     */
	private static int restaurantId1;
	
	/**
     * The ID of the second restaurant for which the report is generated (if applicable).
     */
	private static int restaurantId2;
	
	/**
     * The name of the first restaurant for which the report is generated.
     */
	private static String restaurantName1;
	
	/**
     * The name of the second restaurant for which the report is generated (if applicable).
     */
	private static String restaurantName2;
	
	/**
     * The first selected quarter for which the report is generated.
     */
	private static String quarter1;
	
	/**
     * The second selected quarter for which the report is generated (if applicable).
     */
	private static String quarter2;

	/**
     * Sets the details for the quarterly report.
     * 
     * @param id      the array containing restaurant IDs
     * @param name    the array containing restaurant names
     * @param quarter the array containing the selected quarters
     */
	public static void setDetails(int[] id, String[] name, String[] quarter) {
		restaurantId1 = id[0];
		restaurantId2 = id[1];
		restaurantName1 = name[0];
		restaurantName2 = name[1];
		quarter1 = quarter[0];
		quarter2 = quarter[1];
	}

	/**
     * Starts the quarterly report view interface.
     * 
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML
     */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceo/QuarterReportView2.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ReportViewWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
     * Initializes the controller class. This method is automatically called after
     * the FXML file has been loaded. It sets the restaurant names and requests the 
     * order distribution and total income data for the selected quarters.
     */
	@FXML
	private void initialize() {
		Client.quarterReportView2 = this;

		if (restaurantName2 == null) {
			txtRestaurant.setText(restaurantName1);

			Message msg = new Message(new Object[] { restaurantId1, quarter1 }, Commands.RestaurantQuarterReport2);
			ClientController.client.handleMessageFromClientControllers(msg);

			Message msg2 = new Message(new Object[] { restaurantId1, quarter1 },
					Commands.RestaurantQuarterIncomeReport1);
			ClientController.client.handleMessageFromClientControllers(msg2);

			Message msg1 = new Message(new Object[] { restaurantId1, quarter2 }, Commands.RestaurantQuarterReport3);
			ClientController.client.handleMessageFromClientControllers(msg1);

			Message msg3 = new Message(new Object[] { restaurantId1, quarter2 },
					Commands.RestaurantQuarterIncomeReport2);
			ClientController.client.handleMessageFromClientControllers(msg3);

		} else {
			txtRestaurant.setText(restaurantName1);
			txtRestaurant2.setText(restaurantName2);

			Message msg = new Message(new Object[] { restaurantId1, quarter1 }, Commands.RestaurantQuarterReport2);
			ClientController.client.handleMessageFromClientControllers(msg);

			Message msg2 = new Message(new Object[] { restaurantId1, quarter1 },
					Commands.RestaurantQuarterIncomeReport1);
			ClientController.client.handleMessageFromClientControllers(msg2);

			Message msg1 = new Message(new Object[] { restaurantId2, quarter1 }, Commands.RestaurantQuarterReport3);
			ClientController.client.handleMessageFromClientControllers(msg1);

			Message msg3 = new Message(new Object[] { restaurantId2, quarter1 },
					Commands.RestaurantQuarterIncomeReport2);
			ClientController.client.handleMessageFromClientControllers(msg3);

		}

	}

	/**
     * Handles the server response for the first restaurant's quarterly order report.
     * This method updates the bar chart to display the distribution of the number of orders per day.
     * 
     * @param maxOrders the maximum number of orders in a day
     * @param intervals the intervals for the number of orders per day
     * @param values    the values corresponding to the intervals
     */
	public void handleServerResponseQuarter(int maxOrders, String[] intervals, int[] values) {

		barChart1.getData().clear();
		xAxis1.getCategories().clear();
		barChart1.setAnimated(false);

		barChart1.setTitle("Distribution of Number of Orders per Day");

		// Create a new series
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName(quarter1 + " report");

		for (int i = 0; i < intervals.length; i++) { // origin < intervals.length
			XYChart.Data<String, Number> data = new XYChart.Data<>(intervals[i], values[i]);
			series1.getData().add(data);
		}

		// Set axis labels
		xAxis1.setLabel("orders per day");
		yAxis1.setLabel("day");

		// Add the series to the chart
		barChart1.getData().add(series1);

		// Ensure the chart is refreshed correctly
		barChart1.layout();
	}

	/**
     * Handles the server response for the first restaurant's quarterly income report.
     * This method updates the bar chart to display the total income per week and sets the 
     * total income for the quarter.
     * 
     * @param totalIncome the total income for the quarter
     * @param values      the income values for each week in the quarter
     */
	public void handleServerResponseQuarterIncome(int totalIncome, int[] values) {

		txtTotalIncome.setText("The total income in this quarter is: " + totalIncome + " NIS");

		barChart2.getData().clear();
		xAxis2.getCategories().clear();
		barChart2.setAnimated(false);

		barChart2.setTitle("Total Income per week");

		// Create a new series
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName(quarter1 + " report");

		String[] weeks = new String[12]; // Initialize the array to hold 12 elements
		for (int i = 0; i < 12; i++) {
			weeks[i] = Integer.toString(i + 1); // Convert i+1 to a String and assign it to weeks[i]
		}

		for (int i = 0; i < 12; i++) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(weeks[i], values[i]);
			series1.getData().add(data);

		}

		// Set axis labels
		xAxis2.setLabel("Week");
		yAxis2.setLabel("NIS");

		// Add the series to the chart
		barChart2.getData().add(series1);

		// Ensure the chart is refreshed correctly
		barChart2.layout();
	}

	/**
     * Handles the server response for the second quarterly order report (either the second quarter or second restaurant).
     * This method updates the bar chart to display the distribution of the number of orders per day.
     * 
     * @param maxOrders the maximum number of orders in a day
     * @param intervals the intervals for the number of orders per day
     * @param values    the values corresponding to the intervals
     */
	public void handleServerResponseQuarter1(int maxOrders, String[] intervals, int[] values) {

		barChart3.getData().clear();
		xAxis3.getCategories().clear();
		barChart3.setAnimated(false);

		barChart3.setTitle("Distribution of Number of Orders per Day");

		// Create a new series
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		if (quarter2 != null) {
			series1.setName(quarter2 + " report");
		} else {
			series1.setName(quarter1 + " report");
		}

		for (int i = 0; i < intervals.length; i++) { // origin < intervals.length
			XYChart.Data<String, Number> data = new XYChart.Data<>(intervals[i], values[i]);
			series1.getData().add(data);
		}

		// Set axis labels
		xAxis3.setLabel("orders per day");
		yAxis3.setLabel("day");

		// Add the series to the chart
		barChart3.getData().add(series1);

		// Ensure the chart is refreshed correctly
		barChart3.layout();
	}

	/**
     * Handles the server response for the second quarterly income report (either the second quarter or second restaurant).
     * This method updates the bar chart to display the total income per week and sets the 
     * total income for the quarter.
     * 
     * @param totalIncome the total income for the quarter
     * @param values      the income values for each week in the quarter
     */
	public void handleServerResponseQuarterIncome1(int totalIncome, int[] values) {

		txtTotalIncome1.setText("The total income in this quarter is: " + totalIncome + " NIS");

		barChart4.getData().clear();
		xAxis4.getCategories().clear();
		barChart4.setAnimated(false);

		barChart4.setTitle("Total Income per week");

		// Create a new series
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		if (quarter2 != null) {
			series1.setName(quarter2 + " report");
		} else {
			series1.setName(quarter1 + " report");
		}
		String[] weeks = new String[12]; // Initialize the array to hold 12 elements
		for (int i = 0; i < 12; i++) {
			weeks[i] = Integer.toString(i + 1); // Convert i+1 to a String and assign it to weeks[i]
		}

		for (int i = 0; i < 12; i++) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(weeks[i], values[i]);
			series1.getData().add(data);

		}

		// Set axis labels
		xAxis4.setLabel("Week");
		yAxis4.setLabel("NIS");

		// Add the series to the chart
		barChart4.getData().add(series1);

		// Ensure the chart is refreshed correctly
		barChart4.layout();
	}

	 /**
     * Handles the action for the back button. This method returns the user to the
     * main CEO interface.
     * 
     * @param event the event triggered by the back button click
     * @throws Exception if an error occurs while opening the CEO interface
     */
	@FXML
	void getBackBtn(ActionEvent event) throws Exception {

		((Node) event.getSource()).getScene().getWindow().hide();
		CEOController newScreen = new CEOController();
		newScreen.start(new Stage());
	}

}
