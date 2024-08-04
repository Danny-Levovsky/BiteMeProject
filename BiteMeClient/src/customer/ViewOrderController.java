package customer;

import entites.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;




public class ViewOrderController {

	@FXML
	private Button btnBack;
    @FXML
    private Button btnApprove;
    
    private static int id;
    
	public static void setId(int id1) {
		id = id1;
	}


	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/ViewOrder.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("ViewOrderWindow");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
    @FXML
    void getBtnApprove(ActionEvent event) {

    }

	@FXML
	void getBtnBack(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		CustomerController newScreen = new CustomerController();
		newScreen.start(new Stage());
	}

}
