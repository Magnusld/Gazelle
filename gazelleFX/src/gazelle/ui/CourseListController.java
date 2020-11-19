package gazelle.ui;

import gazelle.api.CourseResponse;
import gazelle.api.UserResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseListController extends BaseController {

    @FXML
    public Button newCourse;
    @FXML
    private Button deleteCourse;
    @FXML
    private VBox courseList;
    private final ArrayList<CourseItemController> controllers = new ArrayList<>();

    private GazelleController app;

    @FXML
    private void initialize() throws IOException {}

    public void onShow() {
        clearView();
        app.sideRun(() -> {
            Long userId = app.getClient().loggedInUserId();
            List<CourseResponse> courses = app.getClient().courses().getOwnedCourses(userId);
            app.mainRun(() -> setCourses(courses));
        });
    }

    public void clearView() {
        courseList.setVisible(false); //TODO: Add spinner
    }

    public void setCourses(List<CourseResponse> courses) {
        // Make enough controllers
        while (controllers.size() < courses.size())
            controllers.add(CourseItemController.load(this));

        // Remove extra controllers
        controllers.subList(courses.size(), controllers.size()).clear();

        // Assign a course per controller
        for (int i = 0; i < courses.size(); i++)
            controllers.get(i).setCourse(courses.get(i));

        // Set list content to the controllers
        courseList
                .getChildren()
                .setAll(controllers.stream()
                        .map(BaseController::getNode)
                        .collect(Collectors.toList()));

        courseList.setVisible(true);
        deleteCourse.setVisible(!courses.isEmpty());
    }

    @FXML
    public void handleNewCourseClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nytt løp");
        dialog.setContentText("Navn");
        dialog.setHeaderText("Lag et nytt løp");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty())
            return;
        String name = result.get();
        if (name.isBlank())
            return;

        clearView();

        app.sideRun(() -> {
            /*try {
                Course course = new Course(name);
                course = app.getSession().postNewCourse(course);
                User user = app.getSession().getLoggedInUser();
                app.getSession()
                        .setUserRoleForCourse(user, course, CourseRoleType.OWNER);
            } catch (Exception e) {
                //TODO: Tell user that something went wrong
                throw e;
            } finally {
                app.mainRun(this::onShow);
            }*/
        });
    }

    @FXML
    public void handleDeleteCourseClick() {

    }

    public static CourseListController load(GazelleController app) {
        CourseListController controller = loadFromFXML("/scenes/courseList.fxml");
        controller.app = app;
        return controller;
    }
}
