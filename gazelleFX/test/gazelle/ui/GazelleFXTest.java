package gazelle.ui;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Scene;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;
import java.io.IOException;

public class GazelleFXTest extends ApplicationTest {

    private GazelleController controller;

    @Override
    public void start(final Stage stage) throws IOException {
        controller = GazelleController.load();
        stage.setScene(new Scene(controller.getNode()));
        stage.show();
    }

    @Test
    public void testControllerShow() {
        assertTrue(controller.getNode().isVisible());
    }
}
