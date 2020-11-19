package gazelle.ui;

import gazelle.api.CourseResponse;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CourseItemController extends BaseController {

    @FXML
    private Text courseTitle;

    private CourseResponse course;

    private CourseListController parent;

    private boolean deleteEnabled = false;
    private boolean markedForDelete = false;

    @FXML
    public void handleClick() {
        if (deleteEnabled) {
            setMarkedForDelete(!markedForDelete);
        } else {
            parent.onCourseSelected(course);
        }
    }

    public void setDeleteEnabled(boolean deleteEnabled) {
        this.deleteEnabled = deleteEnabled;
        setMarkedForDelete(false);
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
        if (markedForDelete)
            getNode().getStyleClass().add("markedForDelete");
        else
            getNode().getStyleClass().remove("markedForDelete");
        parent.onDeleteMarkChanged();
    }

    public boolean markedForDelete() {
        return this.markedForDelete;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
        this.setDeleteEnabled(false);
        courseTitle.setText(course.getName());
    }

    public static CourseItemController load(CourseListController parent) {
        CourseItemController courseItemController = loadFromFXML("/scenes/courseItem.fxml");
        courseItemController.parent = parent;
        return courseItemController;
    }
}
