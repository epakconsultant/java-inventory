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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogAlertActivity extends Stage {
    private final int WIDTH_DEFAULT = 300;

    public DialogAlertActivity(Stage owner, String msg, int type) {
        this.setResizable(false);
        this.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label(msg);
        label.setWrapText(true);
        Button button = new Button("OK");
        button.setOnAction((EventHandler)new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                DialogAlertActivity.this.close();
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setTop((Node)label);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        //hbox.getChildren().add((Object)button);
        borderPane.setBottom((Node)hbox);
        Text text = new Text(msg);
        text.snapshot(null, null);
        int width = (int)text.getLayoutBounds().getWidth() + 50;
        if (width < 300) {
            width = 300;
        }
        Scene scene = new Scene((Parent)borderPane, (double)width, 100.0);
        scene.setFill((Paint)Color.TRANSPARENT);
        this.setScene(scene);
        this.setX(owner.getX() + (owner.getWidth() / 2.0 - (double)(width / 2)));
        this.setY(owner.getY() + (owner.getHeight() / 2.0 - 50.0));
    }

}
