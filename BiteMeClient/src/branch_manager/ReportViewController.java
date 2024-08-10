package branch_manager;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

	private static int restaurantId;
	private static String monthYear;
	private static String district;
	private static String reportType;
	private static String restaurantName;

	public static void setDetails(int id, String month, String dis, String report, String name) {
		restaurantId = id;
		monthYear = month;
		district = dis;
		reportType = report;
		restaurantName = name;
	}

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/branch_manager/ReportView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ReportViewWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	private void initialize() {

		txtRestaurant.setText(restaurantName);

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
	 * 
	 * we need to create an object of the class : ReportViewController scheduler =
	 * new ReportViewController(); scheduler.startScheduler();
	 */
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void startScheduler() {
		// Calculate the initial delay until the end of the current month
		long initialDelay = calculateInitialDelay();

		// Schedule the task to run at the end of every month
		scheduler.scheduleAtFixedRate(this::timer, initialDelay, 30, TimeUnit.DAYS);
	}

	private long calculateInitialDelay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate lastDayOfMonth = now.toLocalDate().withDayOfMonth(now.toLocalDate().lengthOfMonth());
		LocalDateTime lastMomentOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
		long initialDelay = Date.from(lastMomentOfMonth.atZone(ZoneId.systemDefault()).toInstant()).getTime()
				- System.currentTimeMillis();
		return initialDelay;
	}

	private void timer() {
		incomeReport();
		orderReport();
		performanceReport();
	}

	/* end of timer */

	
	public void incomeReport() {

		Commands command = Commands.getIncomeReport;
		Object[] incomeReportData = {restaurantId, monthYear, district};
		Message message = new Message(incomeReportData, command);
		ClientController.client.handleMessageFromClientControllers(message);
		
	}
	
	public void handleServerResponseIncome(int[] incomeReportResultData) {
		//... 
		
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("income report " + monthYear);
		series1.getData().add(new XYChart.Data<>("week1", incomeReportResultData[0]));
		series1.getData().add(new XYChart.Data<>("week2", incomeReportResultData[1]));
		series1.getData().add(new XYChart.Data<>("week3",incomeReportResultData[2]));
		series1.getData().add(new XYChart.Data<>("week4",incomeReportResultData[3]));
		xAxis.setLabel("Week");
		yAxis.setLabel("NIS");
		barChart.getData().addAll(series1);
	}
	
	

	public void orderReport() {
		
		
		

	}

	public void handleServerResponseOrder(Message message) {
		
		//...
		
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("income report " + monthYear);
		series1.getData().add(new XYChart.Data<>("Salad", 250));
		series1.getData().add(new XYChart.Data<>("Main Course", 300));
		series1.getData().add(new XYChart.Data<>("Dessert",60 ));
		series1.getData().add(new XYChart.Data<>("Drink",500));
		xAxis.setLabel("Dish Type");
		yAxis.setLabel("Quantity");
		barChart.getData().addAll(series1);
	}
	
	
	public void performanceReport() {
		//...
		
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("income report " + monthYear);
		series1.getData().add(new XYChart.Data<>("week1", 20));
		series1.getData().add(new XYChart.Data<>("week2", 10));
		series1.getData().add(new XYChart.Data<>("week3",5 ));
		series1.getData().add(new XYChart.Data<>("week4",2 ));
		xAxis.setLabel("Week");
		yAxis.setLabel("%");
		barChart.getData().addAll(series1);
	}
	
	
	public void handleServerResponsePerformance(Message message) {
		
	}

	
	@FXML
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		BranchManagerController newScreen = new BranchManagerController();
		newScreen.start(new Stage());

	}

}
