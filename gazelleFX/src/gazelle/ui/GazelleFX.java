package gazelle.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GazelleFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new VBox(new Button("Hei"))));
        stage.setTitle("Halla");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
