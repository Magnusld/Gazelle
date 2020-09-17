package gazelle.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GazelleController extends BaseController {

    @FXML
    private BorderPane mainPane;



    @FXML
    private void initialize() throws IOException {
        
    }

    public static GazelleController load() throws IOException {
        return loadFromFXML("/scenes/main.fxml");
    }
}
