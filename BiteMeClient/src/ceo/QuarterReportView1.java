package ceo;


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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class QuarterReportView1 {
	
	 @FXML
	    private Button btnBack;

	    @FXML
	    private BarChart<String, Number> barChart;

	    @FXML
	    private CategoryAxis xAxis;

	    @FXML
	    private NumberAxis yAxis;

	    @FXML
	    private Label txtRestaurant; //set restaurant name
	    
	    @FXML
	    private Label txtTotalIncome; //to show total income in given quarter
	    
	    
	    private static int restaurantId;
	    private static String restaurantName;
	    private static String quarter;
	    
	    public static void setDetails(int id, String name, String quarter1) {
	    	restaurantId = id;
	    	restaurantName = name;
	    	quarter = quarter1;
	    }
	    
	    
	    public void start(Stage primaryStage) throws Exception {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceo/QuarterReportView1.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setTitle("ReportViewWindow");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	    
	    @FXML
		private void initialize() {
	    	txtRestaurant.setText(restaurantName);
	    	
			Message msg = new Message(new Object[]{restaurantId, quarter}, Commands. RestaurantQuarterReport1);
	        ClientController.client.handleMessageFromClientControllers(msg); 
	    }    
	    
	    public void handleServerResponse() {
	    	
	    }

	    @FXML
	    void getBackBtn(ActionEvent event) throws Exception {
	    	
	    	((Node) event.getSource()).getScene().getWindow().hide();
	    	CEOController newScreen = new CEOController();
			newScreen.start(new Stage());
	    }

}
