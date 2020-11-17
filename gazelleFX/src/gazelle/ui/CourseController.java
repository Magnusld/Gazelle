package gazelle.ui;

import gazelle.api.CourseResponse;
import gazelle.model.Course;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class CourseController extends BaseController {

    @FXML
    private Text courseTitle;

    private CourseResponse course;

    public CourseResponse getCourse() {
        return course;
    }

    @FXML
    public void handleClick() {

    }

    public void setCourse(CourseResponse course) {
        this.course = course;
        courseTitle.setText(course.getName());
    }

    public static CourseController load(CourseListController parent) {
        CourseController courseController = loadFromFXML("/scenes/course.fxml");
        //courseController.parent = parent;
        return courseController;
    }
}
