package gazelle.ui;


import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GazelleController extends BaseController {

    @FXML
    private VBox contentBox;

    private CourseListController courseListController;

    @FXML
    private void initialize() throws IOException {
        courseListController = CourseListController.load();

        setCurrentScreen(courseListController);
    }

    private void setCurrentScreen(BaseController controller) {
        contentBox.getChildren().setAll(controller.getNode());
    }

    public void onClosing() {

    }

    public static GazelleController load() {
        return loadFromFXML("/scenes/main.fxml");
    }
}
