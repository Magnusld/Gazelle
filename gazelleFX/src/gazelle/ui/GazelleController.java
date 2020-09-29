package gazelle.ui;

import gazelle.model.Course;
import gazelle.model.Database;
import gazelle.model.User;
import gazelle.persistence.DatabaseLoader;
import gazelle.persistence.DatabaseSaver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GazelleController extends BaseController {

    @FXML
    private VBox contentBox;

    private CourseListController courseListController;

    private User user;

    @FXML
    private void initialize() throws IOException {
        loadDatabase();

        courseListController = CourseListController.load();
        courseListController.setData(database, user);

        setCurrentScreen(courseListController);
    }

    private static final String DEFAULT_SAVE_LOCATION = "database.bin";

    private void loadDatabase() {
        try (DatabaseLoader loader = new DatabaseLoader(new FileInputStream(DEFAULT_SAVE_LOCATION))) {
            database = loader.load();
            user = database.getUsers().iterator().next(); //TODO: Don't always log in as random user
        }
        catch(FileNotFoundException e) {
            setupDefaultDatabase();
        }
        catch(IOException e) {
            e.printStackTrace();
            setupDefaultDatabase();
        }
    }

    private void setupDefaultDatabase() {
        //default content
        database = new Database();
        user = database.newUser();
    }

    private void setCurrentScreen(BaseController controller) {
        contentBox.getChildren().setAll(controller.getNode());
    }

    public void onClosing() {
        try (DatabaseSaver saver = new DatabaseSaver(new FileOutputStream(DEFAULT_SAVE_LOCATION))) {
            saver.save(database);
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("Saving failed!");
        }
    }

    public static GazelleController load() throws IOException {
        return loadFromFXML("/scenes/main.fxml");
    }
}
