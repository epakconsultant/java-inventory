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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogUtilsActivity {
    private static boolean confirmResult = false;

    public static void alert(String message, String title) {
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
        //vBox.getChildren().addAll((Object[])new Node[]{label, button});
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

    public static boolean confirm(String message, String title) {
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
       // hBox.getChildren().addAll((Object[])new Node[]{buttonYes, buttonNo});
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        //vBox.getChildren().addAll((Object[])new Node[]{label, hBox});
        Scene scene = new Scene((Parent)vBox);
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setScene(scene);
        buttonYes.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                DialogUtilsActivity.setConfirmResult(true);
                stage.close();
            }
        });
        buttonNo.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                DialogUtilsActivity.setConfirmResult(false);
                stage.close();
            }
        });
        stage.showAndWait();
        return confirmResult;
    }

    private static void setConfirmResult(boolean flag) {
        confirmResult = flag;
    }

}

