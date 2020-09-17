package gazelle.ui;

import gazelle.model.Course;
import gazelle.model.Database;
import gazelle.model.User;
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
    private BaseController currentScreen;

    private Database database;
    private User user;

    @FXML
    private void initialize() throws IOException {
        setupDatabase();

        courseListController = CourseListController.load();
        courseListController.setData(database, user);

        setCurrentScreen(courseListController);
    }

    private void setupDatabase() {
        //default content
        database = new Database();
        user = database.newUser();

        Course c1 = database.newCourse("TDT4101 - Algoritmer");
        Course c2 = database.newCourse("TDT4101 - Noe annet");

        c1.addOwner(user);
        c2.addOwner(user);
    }

    private void setCurrentScreen(BaseController controller) {
        this.currentScreen = controller;
        contentBox.getChildren().setAll(controller.getNode());
    }

    public static GazelleController load() throws IOException {
        return loadFromFXML("/scenes/main.fxml");
    }
}
