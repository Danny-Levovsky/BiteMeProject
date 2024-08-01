package resturant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import login.LoginScreenController;

public class CertifiedEmployeeController {

    @FXML
    private Label txtCertifiedEmployeeName; //must replace with employee name 
    
    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnUpdateMenu;

    @FXML
    private Button BtnViewOrders;

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/CertifiedEmployee.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("CertifiedEmployeeWindow");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
	
    
    @FXML
    void getBtnLogout(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	LoginScreenController newScreen = new LoginScreenController();
    	newScreen.start(new Stage());
    }

    @FXML
    void getBtnUpdateMenu(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	UpdateMenuController newScreen = new UpdateMenuController();
    	newScreen.start(new Stage());

    }
    
    @FXML
    void getBtnVieOrders(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
        EmployeeController employeeScreen = new EmployeeController();
		employeeScreen.start(new Stage());
    }

    @FXML
    void updateName(MouseEvent event) {

    }

}
