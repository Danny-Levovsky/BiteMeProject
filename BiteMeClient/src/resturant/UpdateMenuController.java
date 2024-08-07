package resturant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class UpdateMenuController {

    @FXML
    private Button btnBack;

    @FXML
    private TableView<?> txtTable;

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/UpdateMenu.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("UpDateMenuScreen");
    	primaryStage.setScene(scene);
    	primaryStage.show();
	}
    
    @FXML
    void getBtnBack(ActionEvent event) throws Exception {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
    	newScreen.start(new Stage());

    }

}
