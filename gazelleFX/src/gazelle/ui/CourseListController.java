package gazelle.ui;

import gazelle.model.Course;
import gazelle.model.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseListController extends BaseController {

    @FXML
    private VBox courseList;

    private List<CourseController> controllers = new ArrayList<>();

    private  CourseController courseController;

    @FXML
    public Button newCourse;

    @FXML
    private void initialize() throws IOException {
        courseController = CourseController.load();
        controllers.stream().forEach(e -> courseList.getChildren().setAll(e.getNode()));

    }

    public void setCourses(List<Course> courses) throws IOException {
        courseList.getChildren().clear();
        for (Course course : courses) {
            CourseController courseController = CourseController.load();
            courseController.setCourse(course);

            controllers.add(courseController);
            courseList.getChildren().add(courseController.getNode());
        }
    }

    @FXML
    public void handleNewCourseClick() {
        Database db = new Database();
        Course c1 = db.newCourse("TDT6969 - Memes");
        CourseController courseController = new CourseController();

        courseController.setCourse(c1);
        controllers.add(courseController);
    }

    public static CourseListController load() throws IOException {
        return loadFromFXML("/scenes/courseList.fxml");
    }
}
