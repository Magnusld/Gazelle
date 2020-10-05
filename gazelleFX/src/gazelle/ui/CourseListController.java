package gazelle.ui;

import gazelle.client.GazelleSession;
import gazelle.model.Course;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private VBox courseList;
    private final ArrayList<CourseController> controllers = new ArrayList<>();

    private GazelleController app;

    @FXML
    private void initialize() throws IOException {}

    public void clearView() {
        courseList.setVisible(false); //TODO: Add spinner
    }

    public void setCourses(List<Course> courses) {
        // Make enough controllers
        while (controllers.size() < courses.size())
            controllers.add(CourseController.load());

        // Remove extra controllers
        controllers.subList(courses.size(), controllers.size()).clear();

        // Assign a course per controller
        for (int i = 0; i < courses.size(); i++)
            controllers.get(i).setCourse(courses.get(i));

        // Set list content to the controllers
        courseList
                .getChildren()
                .setAll(controllers.stream().map(e -> e.getNode()).collect(Collectors.toList()));
        courseList.setVisible(true);
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
        //Course newCourse = database.newCourse(name);
        //newCourse.addOwner(user);
        app.showMyCourses();
    }

    public static CourseListController load(GazelleController app) {
        CourseListController controller = loadFromFXML("/scenes/courseList.fxml");
        controller.app = app;
        return controller;
    }
}
