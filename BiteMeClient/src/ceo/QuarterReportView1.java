package ceo;


import client.Client;
import client.ClientController;
import entites.Message;
import enums.Commands;
import javafx.application.Platform;
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
 * Controller class for viewing the quarterly reports in the CEO interface.
 * This class handles the display of order distribution and total income per week 
 * for a selected restaurant and quarter.
 * @author yosra
 */
public class QuarterReportView1 {
	
	 @FXML
	    private Button btnBack;

	    @FXML
	    private BarChart<String, Number> barChart1;

	    @FXML
	    private CategoryAxis xAxis1;

	    @FXML
	    private NumberAxis yAxis1;
	    
	    @FXML
	    private BarChart<String, Number> barChart2;

	    @FXML
	    private CategoryAxis xAxis2;

	    @FXML
	    private NumberAxis yAxis2;

	    @FXML
	    private Label txtRestaurant; //set restaurant name
	    
	    @FXML
	    private Label txtTotalIncome; //to show total income in given quarter
	    
	    
	    private static int restaurantId;
	    private static String restaurantName;
	    private static String quarter;
	    
	    /**
	     * Sets the details for the quarterly report.
	     * @param id the restaurant ID
	     * @param name the name of the restaurant
	     * @param quarter1 the selected quarter
	     */
	    public static void setDetails(int id, String name, String quarter1) {
	    	restaurantId = id;
	    	restaurantName = name;
	    	quarter = quarter1;
	    }
	    
	    /**
	     * Starts the quarterly report view interface.
	     * @param primaryStage the primary stage for this application
	     * @throws Exception if an error occurs during loading the FXML
	     */
	    public void start(Stage primaryStage) throws Exception {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceo/QuarterReportView1.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle("ReportViewWindow");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	    
	    /**
	     * Initializes the controller class. This method is automatically called after
	     * the FXML file has been loaded. It sets the restaurant name and requests the 
	     * order distribution and total income data for the selected quarter.
	     */
	    @FXML
		private void initialize() {
			Client.quarterReportView1 = this;
	    	
	    	txtRestaurant.setText(restaurantName);
	    	
			Message msg = new Message(new Object[]{restaurantId, quarter}, Commands. RestaurantQuarterReport1);
	        ClientController.client.handleMessageFromClientControllers(msg); 
	        
	        
	        Message msg1 = new Message(new Object[]{restaurantId, quarter}, Commands. RestaurantQuarterIncomeReport);
	        ClientController.client.handleMessageFromClientControllers(msg1); 
	    }    
	    
	    /**
	     * Handles the server response for the quarterly order report. This method 
	     * updates the bar chart to display the distribution of the number of orders per day.
	     * @param maxOrders the maximum number of orders in a day
	     * @param intervals the intervals for the number of orders per day
	     * @param values the values corresponding to the intervals
	     */
	    public void handleServerResponseQuarter(int maxOrders, String[] intervals, int[] values) {
	    	
	    	barChart1.getData().clear();
	    	xAxis1.getCategories().clear();
	    	barChart1.setAnimated(false);
	    	
	    	barChart1.setTitle("Distribution of Number of Orders per Day");
	    	
	    	 // Create a new series
		    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		    series1.setName(quarter + " report");
		    
		    for (int i = 0; i < maxOrders+1; i++) {  //origin < intervals.length
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
	     * Handles the server response for the quarterly income report. This method 
	     * updates the bar chart to display the total income per week and sets the total 
	     * income for the quarter.
	     * @param totalIncome the total income for the quarter
	     * @param values the income values for each week in the quarter
	     */
	    public void handleServerResponseQuarterIncome(int totalIncome, int[] values) {

	        txtTotalIncome.setText("The total income in this quarter is: " + totalIncome + " NIS");

	        barChart2.getData().clear();
	        xAxis2.getCategories().clear();
	        barChart2.setAnimated(false);

	        barChart2.setTitle("Total Income per week");

	        // Create a new series
	        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	        series1.setName(quarter + " report");

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
	     * Handles the action for the back button. This method returns the user to the
	     * main CEO interface.
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
