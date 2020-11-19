package gazelle.ui;

import gazelle.api.CourseResponse;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CourseItemController extends BaseController {

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

    public static CourseItemController load(CourseListController parent) {
        CourseItemController courseController = loadFromFXML("/scenes/courseItem.fxml");
        //courseController.parent = parent;
        return courseController;
    }
}
