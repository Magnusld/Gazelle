package gazelle.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GazelleController extends BaseController {

    @FXML
    private VBox contentBox;

    private CourseListController courseListController;

    @FXML
    private void initialize() throws IOException {
        courseListController = CourseListController.load();
        contentBox.getChildren().setAll(courseListController.getNode());
        
    }

    public static GazelleController load() throws IOException {
        return loadFromFXML("/scenes/main.fxml");
    }
}
