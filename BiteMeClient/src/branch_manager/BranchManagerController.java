import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class BranchManagerController {

    @FXML
    private Label txtBranchManagerName; ////must replace with branch manager name 

    @FXML
    private Button btnLogout;

    @FXML
    private Button BtnUpdateClient;

    @FXML
    private Button BtnViewReports;

    @FXML
    private ComboBox<Integer> monthComboBox;
    
    
    @FXML
    public void initialize() {
        // Add month values 1 to 12 to the ComboBox
        for (int i = 1; i <= 12; i++) {
        	monthComboBox.getItems().add(i);
        }
    }
    

    @FXML
    void getBtnLogout(ActionEvent event) {

    }

    @FXML
    void getBtnUpdateClient(ActionEvent event) {

    }

    @FXML
    void getBtnViewReports(ActionEvent event) {

    }

    @FXML
    void updateName(MouseEvent event) {

    }

}
