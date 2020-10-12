package gazelle.ui;

import gazelle.model.Course;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class CourseController extends BaseController {

    private CourseListController parent;

    public enum DeleteState {
        SAFE, // The user is not in deletion mode
        UNDELETABLE("course_undeletable"), // Not possible for user to delete
        DELETABLE("course_deletable"), // Not selected, but could be
        SELECTED("course_selected"); // Selected for deletion

        private String styleClass;

        DeleteState() {
            this(null);
        }

        DeleteState(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    @FXML
    private Text courseTitle;

    private Course course;

    private DeleteState deleteState = DeleteState.SAFE;

    public Course getCourse() {
        return course;
    }

    public void setDeleteState(DeleteState deleteState) {
        if (this.deleteState.getStyleClass() != null)
            getNode().getStyleClass().remove(this.deleteState.getStyleClass());

        this.deleteState = deleteState;

        if (this.deleteState.getStyleClass() != null)
            getNode().getStyleClass().add(this.deleteState.getStyleClass());
    }

    public DeleteState getDeleteState() {
        return deleteState;
    }

    @FXML
    public void handleClick() {
        if (this.deleteState == DeleteState.DELETABLE) {
            this.setDeleteState(DeleteState.SELECTED);
        } else if (this.deleteState == DeleteState.SELECTED) {
            this.setDeleteState(DeleteState.DELETABLE);
        }
        parent.handleSelectedCourseClick();
    }

    public void setCourse(Course course) {
        this.course = course;
        setDeleteState(DeleteState.SAFE);
        courseTitle.setText(course.getName());
    }

    public static CourseController load(CourseListController parent) {
        CourseController courseController = loadFromFXML("/scenes/course.fxml");
        courseController.parent = parent;
        return courseController;
    }
}
