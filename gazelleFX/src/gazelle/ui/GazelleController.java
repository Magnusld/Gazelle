package gazelle.ui;


import gazelle.client.GazelleSession;
import gazelle.model.Course;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GazelleController extends BaseController {

    private static final String API_URI = "http://localhost:8080/";

    @FXML
    private HBox navbar;
    @FXML
    private VBox contentBox;

    private GazelleSession session;

    private LogInController logInController;
    private CourseListController courseListController;

    @FXML
    private void initialize() throws IOException {
        session = new GazelleSession(API_URI);

        logInController = LogInController.load(this);
        courseListController = CourseListController.load(this);

        showLogInScreen(); //TODO: Already logged in?
    }

    private void setCurrentScreen(BaseController controller) {
        contentBox.getChildren().setAll(controller.getNode());
    }

    public GazelleSession getSession() {
        return session;
    }

    public void showLogInScreen() {
        navbar.setVisible(false);
        setCurrentScreen(logInController);
    }

    public void showMyCourses() {
        navbar.setVisible(true);
        courseListController.clearView();
        setCurrentScreen(courseListController);

        sideRun(() -> {
            List<Course> courses = session.getCoursesForUser(session.getLoggedInUser());
            mainRun(()->courseListController.setCourses(courses));
        });
    }

    /**
     * Start a job that should run in the background
     * @param runnable the job
     */
    public void sideRun(Runnable runnable) {
        new Thread(runnable).start();
    }

    /**
     * Add a job to the JavaFX Application Thread queue
     * @param runnable the job
     */
    public void mainRun(Runnable runnable) {
        Platform.runLater(runnable);
    }

    public static GazelleController load() {
        return loadFromFXML("/scenes/main.fxml");
    }
}
