package gazelle.ui;

import gazelle.model.Course;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CourseController extends BaseController {

    @FXML
    private Text courseTitle;

    public void setCourse(Course course) {
        courseTitle.setText(course.getName());
    }

    public static CourseController load() {
        return loadFromFXML("/scenes/course.fxml");
    }
}
