package customer;




import entites.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CustomerController {

    @FXML
    private Label txtCustomerName; 

    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnNewOrder;

    @FXML
    private Button BtnViewOrder;
    
    
    private static User customer;

    
    public static void setCustomer(User user) {
        customer = user;
    }
    
    
  	public void start(Stage primaryStage) throws Exception {
  		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/Customer.fxml"));
      	Parent root = loader.load();
      	Scene scene = new Scene(root);
      	primaryStage.setTitle("CustomerWindow");
      	primaryStage.setScene(scene);
      	primaryStage.show();
  	}
  	
    @FXML
    private void initialize() {
	  	 //update name
	    if(customer != null) {
	    	txtCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
	    }
    }
    

    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnNewOrder(ActionEvent event) {

    }

    @FXML
    void getBtnViewOrder(ActionEvent event) {

    }


    
    


}
