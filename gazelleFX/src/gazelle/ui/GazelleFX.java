package gazelle.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GazelleFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GazelleController main = GazelleController.load();
        stage.setScene(new Scene(main.getNode()));
        stage.setTitle("GazelleFX");
        //stage.setOnCloseRequest(e -> main.onClosing());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
