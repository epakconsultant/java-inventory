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
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import subjectinventory.CategoryTyps;
import subjectinventory.Subject;
import subjectinventory.SubjectInventoryModel;
import subjectinventory.DialogUtilsActivity;

public class AddDialogControllerWindow
implements Initializable {
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField textId;
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textCredit;
    @FXML
    private ComboBox<String> comboCategory;
    private Stage stage;
    private SubjectInventoryModel model;
    private boolean contentChanged = false;
    private String courseCategory;

    public void initialize(URL url, ResourceBundle rb) {
        this.comboCategory.getSelectionModel().selectedItemProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                AddDialogControllerWindow.this.courseCategory = newValue;
                AddDialogControllerWindow.this.contentChanged = true;
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(SubjectInventoryModel model) {
        this.model = model;
        this.populateCategories();
    }

    @FXML
    private void handleButtonAddAction(ActionEvent event) {
        String id = this.textId.getText().toUpperCase();
        if (!this.model.validateCourseId(id)) {
            DialogUtilsActivity.alert("ID must consist of 4 alphabets followed by 5 digit numeric.", "Validation");
            return;
        }
        if (this.model.findCourseIndex(id) >= 0) {
            DialogUtilsActivity.alert("Course ID is alread exist. Use a unique ID.", "Validation");
            return;
        }
        String title = this.textTitle.getText();
        if (title.length() <= 0) {
            DialogUtilsActivity.alert("Please enter the title of the course.", "Validation");
            return;
        }
        int credit = 0;
        try {
            credit = Integer.parseInt(this.textCredit.getText());
        }
        catch (NumberFormatException e) {
            DialogUtilsActivity.alert("Credit must be an integer number.", "Validation");
            return;
        }
        if (credit <= 0) {
            DialogUtilsActivity.alert("Credit must be greater than 0.", "Validation");
            return;
        }
        String cat = (String)this.comboCategory.getValue();
        if (cat == null || cat.length() <= 0) {
            DialogUtilsActivity.alert("Category is not slected.", "Validation");
            return;
        }
        CategoryTyps category = Subject.getCategoryFromString(cat);
        Subject course = new Subject(id, title, credit, category);
        this.model.addCourse(course);
        this.stage.close();
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {
        this.model.setNewCourseId(null);
        this.stage.close();
    }

    private void populateCategories() {
        ObservableList items = this.comboCategory.getItems();
        items.clear();
        int count = this.model.getCategoryCount();
        for (int i = 0; i < count; ++i) {
            items.add((Object)this.model.getCategoryName(i));
        }
    }

}
