package gazelle.ui;


import gazelle.client.GazelleClient;
import gazelle.client.GazelleSession;
import gazelle.model.Course;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GazelleController extends BaseController {

    private static final String API_URI = "http://localhost:8088/";

    @FXML
    private HBox navbar;
    @FXML
    private VBox contentBox;

    private GazelleClient client;

    private LogInController logInController;
    private SignUpController signUpController;
    private CourseListController courseListController;

    @FXML
    private void initialize() throws IOException {
        client = new GazelleClient(API_URI);

        logInController = LogInController.load(this);
        signUpController = SignUpController.load(this);
        courseListController = CourseListController.load(this);

        showLogInScreen(); //TODO: Already logged in?
    }

    private void setCurrentScreen(BaseController controller) {
        contentBox.getChildren().setAll(controller.getNode());
    }

    public GazelleClient getClient() {
        return client;
    }

    public void showLogInScreen() {
        navbar.setVisible(false);
        logInController.onShow();
        setCurrentScreen(logInController);
    }

    public void showSignUpScreen() {
        navbar.setVisible(false);
        signUpController.onShow();
        setCurrentScreen(signUpController);
    }

    public void showMyCourses() {
        navbar.setVisible(true);
        courseListController.onShow();
        setCurrentScreen(courseListController);
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
