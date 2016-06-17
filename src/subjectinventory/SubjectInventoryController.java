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


import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import subjectinventory.AboutDialogControllerWindow;
import subjectinventory.AddDialogControllerWindow;
import subjectinventory.Subject;
import subjectinventory.SubjectInventoryModel;
import subjectinventory.SearchDialogControllerWindow;

public class SubjectInventoryController
implements Initializable {
    @FXML
    private Label labelInfo;
    @FXML
    private MenuItem menuSave;
    @FXML
    private MenuItem menuEdit;
    @FXML
    private MenuItem menuAdd;
    @FXML
    private MenuItem menuDelete;
    @FXML
    private MenuItem menuSearch;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField textTitle;
    @FXML
    private TextField textCredit;
    @FXML
    private ComboBox<String> comboCategory;
    @FXML
    private ComboBox<String> comboCategoryMain;
    @FXML
    private ListView<String> listCourse;
    private Stage stage;
    private SubjectInventoryModel model;
    private File file;
    private File directory;
    private Subject selectedCourse;
    private boolean confirmResult;
    private String selectedCourseId;
    private String selectedCategory;
    private String courseCategory;
    private boolean contentChanged = false;

    public void initialize(URL url, ResourceBundle rb) {
        this.model = new SubjectInventoryModel();
        this.initControls();
        this.labelInfo.setText("Open a course list file from File > Open menu.");
        this.listCourse.getSelectionModel().selectedItemProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                SubjectInventoryController.this.selectedCourseId = newValue;
                SubjectInventoryController.this.showCourseInfo(SubjectInventoryController.this.selectedCourseId);
            }
        });
        this.comboCategoryMain.getSelectionModel().selectedItemProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.equals("All Categories")) {
                    SubjectInventoryController.this.selectedCategory = null;
                } else {
                    SubjectInventoryController.this.selectedCategory = newValue;
                }
                SubjectInventoryController.this.selectedCourseId = null;
                SubjectInventoryController.this.clearCourseInfo();
                SubjectInventoryController.this.populateCourses(SubjectInventoryController.this.selectedCategory);
                SubjectInventoryController.this.labelInfo.setText(newValue + " category is selected.");
            }
        });
        this.comboCategory.getSelectionModel().selectedItemProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                SubjectInventoryController.this.courseCategory = newValue;
                SubjectInventoryController.this.contentChanged = true;
            }
        });
        this.textTitle.textProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                SubjectInventoryController.this.contentChanged = true;
            }
        });
        this.textCredit.textProperty().addListener((ChangeListener)new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                SubjectInventoryController.this.contentChanged = true;
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest((EventHandler)new EventHandler<WindowEvent>(){

            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit the program?", new ButtonType[0]);
                Optional result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                } else {
                    event.consume();
                }
            }
        });
    }

    @FXML
    private void handleMenuOpenAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Course List File...");
        //fileChooser.getExtensionFilters().addAll((Object[])new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("All Files", new String[]{"*.*"}), new FileChooser.ExtensionFilter("DAT", new String[]{"*.dat"}), new FileChooser.ExtensionFilter("TXT", new String[]{"*.txt"})});
        this.file = fileChooser.showOpenDialog((Window)this.stage);
        if (this.file == null) {
            return;
        }
        try {
            this.directory = new File(this.file.getCanonicalPath());
        }
        catch (IOException e) {
            this.directory = null;
        }
        this.initControls();
        this.model.readCourseFile(this.file);
        if (this.model.getCourseCount() > 0) {
            this.enableControls();
            this.selectedCategory = null;
            this.selectedCourseId = null;
            this.populateCategories();
            this.populateCourses(null);
            this.buttonAdd.setDisable(false);
        }
        this.labelInfo.setText("Loaded " + this.model.getCourseCount() + " courses from " + this.file.getName());
    }

    @FXML
    private void handleMenuSaveAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course List File As...");
       // fileChooser.getExtensionFilters().addAll((Object[])new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("All Files", new String[]{"*.*"}), new FileChooser.ExtensionFilter("DAT", new String[]{"*.dat"}), new FileChooser.ExtensionFilter("TXT", new String[]{"*.txt"})});
        this.file = fileChooser.showSaveDialog((Window)this.stage);
        if (this.file == null) {
            return;
        }
        try {
            this.directory = new File(this.file.getCanonicalPath());
            this.model.saveCourseFile(this.file);
        }
        catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        this.labelInfo.setText("Saved " + this.model.getCourseCount() + " courses to " + this.file.getName() + ".");
    }

    @FXML
    private void handleMenuExitAction(ActionEvent event) {
        boolean result = this.confirm("Do you want to exit program?", "Confirmation");
        if (!result) {
            return;
        }
        Platform.exit();
    }

    @FXML
    private void handleMenuEditAction(ActionEvent event) {
        this.handleButtonEditAction(event);
    }

    @FXML
    private void handleMenuAddAction(ActionEvent event) {
        this.handleButtonAddAction(event);
    }

    @FXML
    private void handleMenuDeleteAction(ActionEvent event) {
        this.handleButtonDeleteAction(event);
    }

    @FXML
    private void handleMenuSearchAction(ActionEvent event) {
        this.handleButtonSearchAction(event);
    }

    @FXML
    private void handleMenuAboutAction(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("AboutDialog.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("About");
        stage.setScene(new Scene(root));
        stage.show();
        AboutDialogControllerWindow ctrlAbout = (AboutDialogControllerWindow)fxmlLoader.getController();
        ctrlAbout.setStage(stage);
    }

    @FXML
    private void handleButtonEditAction(ActionEvent event) {
        this.textTitle.setDisable(false);
        this.textCredit.setDisable(false);
        this.comboCategory.setDisable(false);
        this.buttonSave.setDisable(false);
        this.buttonCancel.setDisable(false);
        this.buttonSearch.setDisable(true);
        this.buttonAdd.setDisable(true);
        this.buttonDelete.setDisable(true);
        this.comboCategoryMain.setDisable(true);
        this.listCourse.setDisable(true);
        this.menuSearch.setDisable(true);
        this.menuAdd.setDisable(true);
        this.menuDelete.setDisable(true);
        this.contentChanged = false;
        this.labelInfo.setText("Modify title, credit and category, then click \"Save\" or \"Cancel\" button.");
    }

    @FXML
    private void handleButtonAddAction(ActionEvent event) {
        String courseId = this.showAddDialog();
        if (courseId == null) {
            return;
        }
        this.populateCourses(null);
        this.comboCategoryMain.getSelectionModel().select(0);
        int index = this.model.findCourseIndex(courseId);
        this.listCourse.getSelectionModel().select(index);
        this.listCourse.getFocusModel().focus(index);
        this.listCourse.scrollTo(index);
        this.labelInfo.setText("Added a new course " + courseId + " is added to the list.");
        this.model.saveCourseFile(this.file);
    }

    @FXML
    private void handleButtonDeleteAction(ActionEvent event) {
        String id = (String)this.listCourse.getSelectionModel().getSelectedItem();
        if (id == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the course " + id + " ?", new ButtonType[0]);
        Optional result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.model.removeCourse(id);
            this.model.saveCourseFile(this.file);
            this.comboCategoryMain.getSelectionModel().select(0);
            this.populateCourses(null);
            this.listCourse.getSelectionModel().clearSelection();
            this.labelInfo.setText("Deleted " + id + " from the list.");
        }
    }

    @FXML
    private void handleButtonSearchAction(ActionEvent event) {
        String courseId = this.showSearchDialog();
        if (courseId != null) {
            this.comboCategoryMain.getSelectionModel().select(0);
            int index = this.model.findCourseIndex(courseId);
            this.listCourse.getSelectionModel().select(index);
            this.listCourse.getFocusModel().focus(index);
            this.listCourse.scrollTo(index);
            Subject course = this.model.getCourse(index);
            this.textTitle.setText(course.getTitle());
            this.textCredit.setText(String.valueOf(course.getCredit()));
           // this.comboCategory.getSelectionModel().select((Object)course.getCategoryName());
            this.buttonEdit.setDisable(false);
            this.buttonDelete.setDisable(false);
            this.menuEdit.setDisable(false);
            this.menuDelete.setDisable(false);
            this.labelInfo.setText("Selected " + course.getId());
        } else {
            this.selectedCourseId = null;
            this.selectedCategory = null;
            this.populateCategories();
            this.populateCourses(null);
            this.buttonEdit.setDisable(true);
            this.buttonDelete.setDisable(true);
            this.menuEdit.setDisable(true);
            this.menuDelete.setDisable(true);
            this.labelInfo.setText("Search was cancelled");
        }
    }

    @FXML
    private void handleButtonSaveAction(ActionEvent event) {
        boolean saved = this.confirm("Do you want to save the changes", "Confirmation");
        if (this.contentChanged) {
            String title = this.textTitle.getText();
            if (title == null || title.length() <= 0) {
                this.alert("The title cannot be empty.", "Validation");
                return;
            }
            int credit = 0;
            try {
                credit = Integer.parseInt(this.textCredit.getText());
            }
            catch (NumberFormatException e) {
                this.alert("Credit must be an integer number.", "Validation");
                return;
            }
            if (credit <= 0) {
                this.alert("Credit must be greater than 0.", "Validation");
                return;
            }
            String cat = (String)this.comboCategory.getValue();
            if (cat == null || cat.length() <= 0) {
                this.alert("Category is not slected.", "Validation");
                return;
            }
            this.model.updateCourse(this.selectedCourseId, title, credit, cat);
            this.model.saveCourseFile(this.file);
            this.labelInfo.setText("Updated the course " + this.selectedCourseId);
        }
        this.buttonSave.setDisable(true);
        this.buttonCancel.setDisable(true);
        this.textTitle.setDisable(true);
        this.textCredit.setDisable(true);
        this.comboCategory.setDisable(true);
        this.buttonSearch.setDisable(false);
        this.buttonAdd.setDisable(false);
        this.buttonDelete.setDisable(false);
        this.comboCategoryMain.setDisable(false);
        this.listCourse.setDisable(false);
        this.menuSearch.setDisable(false);
        this.menuAdd.setDisable(false);
        this.menuDelete.setDisable(false);
    }

    @FXML
    private void handleButtonCancelAction(ActionEvent event) {
        this.buttonSave.setDisable(true);
        this.buttonCancel.setDisable(true);
        this.textTitle.setDisable(true);
        this.textCredit.setDisable(true);
        this.comboCategory.setDisable(true);
        this.buttonSearch.setDisable(false);
        this.buttonAdd.setDisable(false);
        this.buttonDelete.setDisable(false);
        this.comboCategoryMain.setDisable(false);
        this.listCourse.setDisable(false);
        this.menuSearch.setDisable(false);
        this.menuAdd.setDisable(false);
        this.menuDelete.setDisable(false);
        //this.listCourse.getSelectionModel().select((Object)this.selectedCourseId);
        this.labelInfo.setText("Cancelled editing " + this.selectedCourseId);
    }

    @FXML
    private void handleComboCategoryMainAction(ActionEvent event) {
    }

    @FXML
    private void handleComboCategoryAction(ActionEvent event) {
    }

    private void initControls() {
        this.comboCategoryMain.setDisable(true);
        this.comboCategoryMain.getSelectionModel().clearSelection();
        this.comboCategoryMain.getItems().clear();
        this.listCourse.setDisable(true);
        this.listCourse.getSelectionModel().clearSelection();
        this.listCourse.getItems().clear();
        this.buttonEdit.setDisable(true);
        this.buttonAdd.setDisable(true);
        this.buttonDelete.setDisable(true);
        this.buttonSearch.setDisable(true);
        this.buttonSave.setDisable(true);
        this.buttonCancel.setDisable(true);
        this.textTitle.setDisable(true);
        this.textTitle.clear();
        this.textCredit.setDisable(true);
        this.textCredit.clear();
        this.comboCategory.setDisable(true);
        this.comboCategory.getSelectionModel().clearSelection();
        this.comboCategory.getItems().clear();
        this.menuSearch.setDisable(true);
        this.menuAdd.setDisable(true);
        this.menuEdit.setDisable(true);
        this.menuDelete.setDisable(true);
    }

    private void enableControls() {
        this.comboCategoryMain.setDisable(false);
        this.listCourse.setDisable(false);
        this.buttonSearch.setDisable(false);
        this.buttonAdd.setDisable(false);
        this.menuSearch.setDisable(false);
        this.menuAdd.setDisable(false);
    }

    private void enableEditCourse() {
        this.textTitle.setDisable(false);
        this.textCredit.setDisable(false);
    }

    private void disableEditCourse() {
        this.textTitle.setDisable(true);
        this.textCredit.setDisable(true);
    }

    private void showCourseInfo(String courseId) {
        Subject course = this.model.getCourse(courseId);
        if (course == null) {
            return;
        }
        this.textTitle.setText(course.getTitle());
        this.textCredit.setText(String.valueOf(course.getCredit()));
        //this.comboCategory.getSelectionModel().select((Object)course.getCategoryName());
        this.buttonEdit.setDisable(false);
        this.buttonAdd.setDisable(false);
        this.buttonDelete.setDisable(false);
        this.menuEdit.setDisable(false);
        this.menuAdd.setDisable(false);
        this.menuDelete.setDisable(false);
    }

    private void clearCourseInfo() {
        this.listCourse.getSelectionModel().select(-1);
        this.textTitle.clear();
        this.textCredit.clear();
        //this.comboCategory.getSelectionModel().select((Object)"");
        this.buttonEdit.setDisable(true);
        this.buttonDelete.setDisable(true);
        this.menuEdit.setDisable(true);
        this.menuDelete.setDisable(true);
    }

    private void populateCourses(String category) {
        ObservableList items = this.listCourse.getItems();
        items.clear();
        int count = 0;
        if (category == null || category.equals("All Cagtegories")) {
            this.listCourse.setItems(FXCollections.observableArrayList(this.model.getCourseIds()));
        } else {
            ArrayList<Subject> courses = this.model.findCoursesByCategory(category);
            count = courses.size();
            for (int i = 0; i < count; ++i) {
                items.add((Object)courses.get(i).getId());
            }
        }
    }

    private void populateCategories() {
        int i;
        ObservableList items = this.comboCategoryMain.getItems();
        items.clear();
       // this.comboCategoryMain.getItems().add((Object)"All Categories");
        int count = this.model.getCategoryCount();
        for (i = 0; i < count; ++i) {
            items.add((Object)this.model.getCategoryName(i));
        }
        this.comboCategoryMain.getSelectionModel().select(0);
        items = this.comboCategory.getItems();
        items.clear();
        count = this.model.getCategoryCount();
        for (i = 0; i < count; ++i) {
            items.add((Object)this.model.getCategoryName(i));
        }
    }

    private String showSearchDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("SearchDialog.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Seach Courses");
            stage.setScene(new Scene(root));
            SearchDialogControllerWindow ctrlSearch = (SearchDialogControllerWindow)fxmlLoader.getController();
            ctrlSearch.setStage(stage);
            ctrlSearch.setModel(this.model);
            stage.showAndWait();
        }
        catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return this.model.getSelectedCourseId();
    }

    private String showAddDialog() {
        this.model.setNewCourseId(null);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("AddDialog.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Course");
            stage.setScene(new Scene(root));
            AddDialogControllerWindow ctrlAdd = (AddDialogControllerWindow)fxmlLoader.getController();
            ctrlAdd.setStage(stage);
            ctrlAdd.setModel(this.model);
            stage.showAndWait();
        }
        catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return this.model.getNewCourseId();
    }

    private void alert(String message, String title) {
        int MIN_WIDTH = 200;
        int MIN_HEIGHT = 50;
        Text text = new Text(message);
        text.snapshot(null, null);
        int width = (int)text.getLayoutBounds().getWidth();
        if (width < 200) {
            width = 200;
        }
        int height = 50;
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPrefWidth((double)width);
        label.setPrefHeight((double)height);
        Button button = new Button("OK");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
       // vBox.getChildren().addAll((Object[])new Node[]{label, button});
        Scene scene = new Scene((Parent)vBox);
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        stage.show();
    }

    private boolean confirm(String message, String title) {
        int MIN_WIDTH = 200;
        int MIN_HEIGHT = 50;
        Text text = new Text(message);
        text.snapshot(null, null);
        int width = (int)text.getLayoutBounds().getWidth();
        if (width < 200) {
            width = 200;
        }
        int height = 50;
        Label label = new Label(message);
        label.setWrapText(true);
        label.setPrefWidth((double)width);
        label.setPrefHeight((double)height);
        Button buttonYes = new Button("Yes");
        buttonYes.setPrefWidth(50.0);
        Button buttonNo = new Button("No");
        buttonNo.setPrefWidth(50.0);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20.0);
      //  hBox.getChildren().addAll((Object[])new Node[]{buttonYes, buttonNo});
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
     //   vBox.getChildren().addAll((Object[])new Node[]{label, hBox});
        Scene scene = new Scene((Parent)vBox);
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        buttonYes.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                SubjectInventoryController.this.setConfirmResult(true);
                stage.close();
            }
        });
        buttonNo.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                SubjectInventoryController.this.setConfirmResult(false);
                stage.close();
            }
        });
        stage.showAndWait();
        return this.confirmResult;
    }

    private void setConfirmResult(boolean flag) {
        this.confirmResult = flag;
    }

    @FXML
    private void test(ActionEvent event) {
        this.showSearchDialog();
    }

}