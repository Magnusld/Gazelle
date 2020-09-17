package gazelle.ui;

import gazelle.model.Course;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

public class CourseController extends BaseController {

    private Course course;

    @FXML
    private Text courseTitle;

    public void setCourse(Course course) {
        this.course = course;
        courseTitle.setText(course.getName());
    }

    public static CourseController load() {
        return loadFromFXML("/scenes/course.fxml");
    }
}
