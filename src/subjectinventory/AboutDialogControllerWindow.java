/*  Name:  [your name here]
    Assignment:  [assignment name]
    Program: [your program name here]
    Date:  [assignment due date here]

    Description:
    [program description in your own words]
*/


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package subjectinventory;

/**
 *
 * @author meraj
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class AboutDialogControllerWindow
implements Initializable {
    private Stage stage;

  
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void handleButtonCloseAction(ActionEvent event) {
        if (this.stage != null) {
            this.stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}


