package gazelle.ui;


import gazelle.client.GazelleClient;
import gazelle.client.GazelleSession;
import gazelle.model.Course;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GazelleController extends BaseController {

    private static final String API_URI = "http://localhost:8088/";

    @FXML
    private HBox navbar;
    @FXML
    private VBox contentBox;

    private static final String[] NAV_LINES = new String[] {
        "#myCoursesLine", "#followedLine", "#focusListLine"
    };

    private GazelleClient client;

    private LogInController logInController;
    private SignUpController signUpController;
    private CourseListController courseListController;
    private CoursePageController coursePageController;
    private PostEditController postEditController;
    private PostPageController postPageController;
    private FocusListController focusListController;

    @FXML
    private void initialize() {
        client = new GazelleClient(API_URI);

        logInController = LogInController.load(this);
        signUpController = SignUpController.load(this);
        courseListController = CourseListController.load(this);
        coursePageController = CoursePageController.load(this);
        //postPageController = PostPageController.load(this);
        postEditController = PostEditController.load(this);
        focusListController = FocusListController.load(this);

        showLogInScreen(); //TODO: Already logged in?
    }

    public GazelleClient getClient() {
        return client;
    }

    private void setCurrentScreen(BaseController controller) {
        contentBox.getChildren().setAll(controller.getNode());
    }

    private void setNavSelection(int index) {
        for (String line : NAV_LINES)
            getNode().lookup(line).getStyleClass().remove("navBarSelected");
        if (index >= 0 && index < NAV_LINES.length)
            getNode().lookup(NAV_LINES[index]).getStyleClass().add("navBarSelected");
        navbar.setVisible(true);
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

    @FXML
    public void showMyCourses() {
        setNavSelection(0);
        courseListController.onShow(true);
        setCurrentScreen(courseListController);
    }

    @FXML
    public void showFollowedCourses() {
        setNavSelection(1);
        courseListController.onShow(false);
        setCurrentScreen(courseListController);
    }

    @FXML
    public void showFocusList() {
        setNavSelection(2);
        focusListController.onShow();
        setCurrentScreen(focusListController);
    }

    @FXML
    public void onSearchEntered() {
        
    }

    public void showCourseScreen(Long courseId) {
        setNavSelection(-1);
        coursePageController.onShow(courseId);
        setCurrentScreen(coursePageController);
    }

    public void showPostScreen(Long postId) {
        setNavSelection(-1);
        postPageController.onShow(postId);
        setCurrentScreen(postPageController);
    }

    public void showPostEditScreen(Long postId) {
        setNavSelection(-1);
        postEditController.onEditExistingPost(postId);
        setCurrentScreen(postEditController);
    }

    public void showNewPostScreen(Long courseId) {
        setNavSelection(-1);
        postEditController.onEditNewPost(courseId);
        setCurrentScreen(postEditController);
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
