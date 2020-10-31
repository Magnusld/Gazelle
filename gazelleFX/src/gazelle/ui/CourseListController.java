package gazelle.ui;

import gazelle.model.Course;
import gazelle.model.User;
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
    private final ArrayList<CourseController> controllers = new ArrayList<>();

    private boolean isDeleting = false;

    private GazelleController app;

    @FXML
    private void initialize() throws IOException {}

    public void onShow() {
        clearView();
        app.sideRun(() -> {
            User user = app.getSession().getLoggedInUser();
            //List<Course> courses = app.getSession().getCoursesForUser(user, CourseRoleType.OWNER);
            //app.mainRun(() -> setCourses(courses));
        });
    }

    public void clearView() {
        courseList.setVisible(false); //TODO: Add spinner
    }

    public void setCourses(List<Course> courses) {
        // Make enough controllers
        while (controllers.size() < courses.size())
            controllers.add(CourseController.load(this));

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
        setDeleting(false);
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
            try {
                Course course = new Course(name);
                course = app.getSession().postNewCourse(course);
                User user = app.getSession().getLoggedInUser();
                //app.getSession()
                //        .setUserRoleForCourse(user, course, CourseRoleType.OWNER);
            } catch (Exception e) {
                //TODO: Tell user that something went wrong
                throw e;
            } finally {
                app.mainRun(this::onShow);
            }
        });
    }

    private void setDeleting(boolean deleting) {
        this.isDeleting = deleting;
        if (this.isDeleting) {
            deleteCourse.getStyleClass().add("deleteIconText");
            deleteCourse.setText("Slett 0 løp");
            controllers.forEach(controller -> {
                controller.setDeleteState(CourseController.DeleteState.DELETABLE);
            });
        } else {
            deleteCourse.getStyleClass().remove("deleteIconText");
            deleteCourse.setText("");
            controllers.forEach(controller -> {
                controller.setDeleteState(CourseController.DeleteState.SAFE);
            });
        }
    }

    private void commitDelete() {
        ArrayList<CourseController> controllersToDelete = getSelectedCourses();
        if (controllersToDelete.size() == 0)
            return;
        this.clearView();
        app.sideRun(() -> {
            try {
                controllersToDelete.forEach(controller -> {
                    app.getSession().deleteCourse(controller.getCourse());
                });
            } catch (Exception e) {
                //TODO: Tell user that something went wrong
                throw e;
            } finally {
                app.mainRun(this::onShow);
            }
        });
    }

    @FXML
    public void handleDeleteCourseClick() {
        if (this.isDeleting)
            commitDelete();
        setDeleting(!this.isDeleting);
    }

    private ArrayList<CourseController> getSelectedCourses() {
        return controllers.stream()
                .filter(controller -> controller.getDeleteState()
                        == CourseController.DeleteState.SELECTED)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void handleCourseClick() {
        if (isDeleting) {
            ArrayList<CourseController> selectedCourses = getSelectedCourses();
            deleteCourse.setText("Slett " + selectedCourses.size() + " løp");
        }
    }

    public static CourseListController load(GazelleController app) {
        CourseListController controller = loadFromFXML("/scenes/courseList.fxml");
        controller.app = app;
        return controller;
    }
}
