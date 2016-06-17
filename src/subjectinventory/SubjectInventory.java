/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subjectinventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author meraj
 */
public class SubjectInventory extends Application {
    

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("SubjectInventoryForm.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Course Inventory");
        stage.setScene(scene);
        SubjectInventoryController ctrlMain = (SubjectInventoryController)fxmlLoader.getController();
        ctrlMain.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        SubjectInventory.launch((String[])args);
    }
}
