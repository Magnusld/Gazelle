package gazelle.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.Objects;

public class BaseController {
    protected Parent node;

    public Parent getNode() {
        return node;
    }

    public static <T extends BaseController> T loadFromFXML(String fxmlPath) {
        try {
            final FXMLLoader fxmlLoader =
                    new FXMLLoader(BaseController.class.getResource(fxmlPath));
            Parent parent = fxmlLoader.load();
            T controller = fxmlLoader.getController();
            controller.node = parent;
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed loading from FXML: " + fxmlPath, e);
        }
    }

    public static <T extends BaseController> T loadFromFXML(String fxmlPath, T controller) {
        try {
            final FXMLLoader fxmlLoader =
                    new FXMLLoader(BaseController.class.getResource(fxmlPath));
            fxmlLoader.setController(controller);
            Parent parent = fxmlLoader.load();
            controller.node = parent;
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed loading from FXML: " + fxmlPath, e);
        }
    }
}
