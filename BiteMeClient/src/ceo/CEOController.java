package ceo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CEOController {

    @FXML
    private Label txtCEOName; //must replace with CEO name

    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnViewQuarterReports;

    @FXML
    private Button BtnViewBranchReports;

    @FXML
    private ComboBox<String> branchComboBox;

    @FXML
    private ComboBox<Integer> monthComboBox;

    @FXML
    private ComboBox<Integer> quarterComboBox;
    
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceo/CEO.fxml"));
    	Parent root = loader.load();
    	Scene scene = new Scene(root);
    	primaryStage.setTitle("CEOWindow");
    	primaryStage.setScene(scene);
    	//RemoveTopBar(primaryStage,root);
    	primaryStage.show();
	}
    
    
    
    @FXML
    public void initialize() {
        // Add month values 1 to 12 to the ComboBox
        for (int i = 1; i <= 12; i++) {
        	monthComboBox.getItems().add(i);
        }
        branchComboBox.getItems().addAll("north", "center", "south");
        quarterComboBox.getItems().addAll(1, 2, 3, 4);
    }

    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnViewBranchReports(ActionEvent event) {

    }

    @FXML
    void getBtnViewQuarterReports(ActionEvent event) {

    }

    @FXML
    void updateName(MouseEvent event) {

    }

}
