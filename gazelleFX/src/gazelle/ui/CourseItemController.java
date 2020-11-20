package gazelle.ui;

import gazelle.api.ChoreResponse;
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

    protected CourseItemController(CourseListController parent) {
        super(parent);
    }

    protected void onItemChanged(CourseResponse course) {
        courseTitle.setText(course.getName());

        doneText.setText("");
        focusedText.setText("");
        currentText.setText("");
        PostResponse currentPost = course.getCurrentPost();
        if (currentPost != null) {
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
        PostResponse previousPost = course.getPreviousPost();
        if (previousPost != null)
            previousText.setText(String.format("Forrige: %s", previousPost.getTitle()));
    }

    public static CourseItemController load(CourseListController parent) {
        return loadFromFXML("/scenes/courseItem.fxml", new CourseItemController(parent));
    }
}
