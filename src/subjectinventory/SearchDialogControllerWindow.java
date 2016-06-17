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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import subjectinventory.Subject;
import subjectinventory.SubjectInventoryModel;

public class SearchDialogControllerWindow
implements Initializable {
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonSelect;
    @FXML
    private Button buttonCancel;
    @FXML
    private RadioButton radioId;
    @FXML
    private RadioButton radioTitle;
    @FXML
    private TextField textSearch;
    @FXML
    private ListView<String> listCourse;
    private Stage stage;
    private int searchType = 0;
    private SubjectInventoryModel model;
    private String selectedCourseId;

    public void initialize(URL url, ResourceBundle rb) {
        this.listCourse.getSelectionModel().selectedItemProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    SearchDialogControllerWindow.this.selectedCourseId = null;
                } else {
                    SearchDialogControllerWindow.this.selectedCourseId = newValue.substring(0, newValue.indexOf(":"));
                }
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(SubjectInventoryModel model) {
        this.model = model;
    }

    @FXML
    private void handleButtonSearchAction(ActionEvent event) {
        ArrayList<Subject> matchedCourses = this.searchType == 0 ? this.model.findCoursesById(this.textSearch.getText()) : this.model.findCoursesByTitle(this.textSearch.getText());
        int count = matchedCourses.size();
        ObservableList items = this.listCourse.getItems();
        items.clear();
        for (int i = 0; i < count; ++i) {
            Subject c = matchedCourses.get(i);
            items.add((Object)(c.getId() + ": " + c.getTitle()));
        }
    }

    @FXML
    private void handleButtonSelectAction(ActionEvent event) {
        this.model.setSelectedCourseId(this.selectedCourseId);
        this.stage.close();
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {
        this.model.setSelectedCourseId(null);
        this.listCourse.getItems().clear();
        this.stage.close();
    }

    @FXML
    private void handleRadioIdAction(ActionEvent event) {
        this.searchType = 0;
    }

    @FXML
    private void handleRadioTitleAction(ActionEvent event) {
        this.searchType = 1;
    }

}


