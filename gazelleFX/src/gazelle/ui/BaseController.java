package gazelle.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class BaseController {
    protected Parent node;
    public Parent getNode() {
        return node;
    }

    public static <T extends BaseController> T loadFromFXML(String fxmlPath) throws IOException {
        System.out.println("DEBUGGGGGGG ====================== " + BaseController.class.getResource(fxmlPath));
        final FXMLLoader fxmlLoader = new FXMLLoader(BaseController.class.getResource(fxmlPath));
        Parent parent = fxmlLoader.load();
        T controller = fxmlLoader.getController();
        controller.node = parent;
        return controller;
    }
}
