package customer;




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
    private Label txtCustomerName;  //must replace with customer name 

    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnNewOrder;

    @FXML
    private Button BtnViewOrder;

    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnNewOrder(ActionEvent event) {

    }

    @FXML
    void getBtnViewOrder(ActionEvent event) {

    }

    @FXML
    void updateName(MouseEvent event) {

    }
    
    
  	public void start(Stage primaryStage) throws Exception {
  		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/Customer.fxml"));
      	Parent root = loader.load();
      	Scene scene = new Scene(root);
      	primaryStage.setTitle("CustomerWindow");
      	primaryStage.setScene(scene);
      	primaryStage.show();
  	}

}
