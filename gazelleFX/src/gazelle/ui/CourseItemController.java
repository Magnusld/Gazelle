package gazelle.ui;

import gazelle.api.CourseResponse;
import gazelle.api.PostResponse;
import gazelle.ui.list.ListController;
import gazelle.ui.list.ListItemController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CourseItemController extends ListItemController<CourseResponse> {

    @FXML
    private Text courseTitle;
    @FXML
    private Text currentText;
    @FXML
    private Text doneText;
    @FXML
    private Text focusedText;
    @FXML
    private Text previousText;
    @FXML
    private Text nextDueDateText;

    protected CourseItemController(CourseListController parent) {
        super(parent);
    }

    protected void onItemChanged(CourseResponse course) {
        courseTitle.setText(course.getName());

        currentText.setText("");
        doneText.setText("");
        focusedText.setText("");
        if (course.getCurrentPost() != null) {
            PostResponse currentPost = course.getCurrentPost();
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), currentPost.getEndDate()) + 1;
            String current = String.format("Nå: %s (%d dager igjen)",
                    currentPost.getTitle(), daysLeft);
            currentText.setText(current);
            doneText.setText(String.format("Gjort: %d / %d",
                    currentPost.getChoresDone(), currentPost.getChoresCount()));
            Integer focused = currentPost.getChoresFocused();
            if (focused != null && focused > 0)
                focusedText.setText(String.format("(%d fokusert)", focused));
        }

        previousText.setText("");
        if (course.getPreviousPost() != null)
            previousText.setText(String.format("Forrige: %s", course.getPreviousPost().getTitle()));

        nextDueDateText.setText("");
        if (course.getNextChoreDue() != null) {
            LocalDate due = course.getNextChoreDue().getDueDate();
            long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), due);
            if (daysUntil == 0) {
                nextDueDateText.setText("Neste first: I dag!");
            } else {
                nextDueDateText.setText(String.format("Neste frist: %d dager", daysUntil));
            }
        }
    }

    public static CourseItemController load(CourseListController parent) {
        return loadFromFXML("/scenes/courseItem.fxml", new CourseItemController(parent));
    }
}
