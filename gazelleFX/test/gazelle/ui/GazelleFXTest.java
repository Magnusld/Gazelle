package gazelle.ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GazelleFXTest extends ApplicationTest {

    private Parent parent;
    //private AppController controller;

    @Override
    public void start(final Stage stage) throws Exception {
        //final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("App.fxml"));
        //parent = fxmlLoader.load();
        //controller = fxmlLoader.getController();
        //stage.setScene(new Scene(parent));
        stage.setScene(new Scene(new VBox(new Button("Hei"))));
        stage.show();
    }

    //@Test
    //public void testController() {
        /*Button button = (Button) parent.lookup("#button");
        clickOn(button);
        assertEquals("Hei", button.getText());*/
    //}
}
