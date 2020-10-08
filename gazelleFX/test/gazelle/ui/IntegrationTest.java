package gazelle.ui;

import static org.junit.jupiter.api.Assertions.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.Map;

public class IntegrationTest extends ApplicationTest {

    private Parent parent;
    private GazelleController app;

    @Override
    public void start(final Stage stage) throws Exception {
        app = GazelleController.load();
        stage.setScene(new Scene(app.getNode()));
        stage.setTitle("GazelleFX");
        stage.show();
    }

    @Test
    public void testLogInFields() {
        TextField textField = (TextField) app.getNode().lookup("#username");
        assertEquals(textField.getText(), "");
        textField.setText("afg");
        assertEquals(textField.getText(), "afg");

        textField = (TextField) app.getNode().lookup("#password");
        assertEquals(textField.getText(), "");
        textField.setText("afg");
        assertEquals(textField.getText(), "afg");
    }
}
