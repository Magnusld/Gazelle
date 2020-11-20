package gazelle.ui;

import gazelle.api.PostResponse;
import gazelle.ui.list.ListItemController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PostItemController extends ListItemController<PostResponse> {

    @FXML
    private Text postTitle;

    @FXML
    private Text descriptionText;

    @FXML
    private Text doneText;

    @FXML
    private Text focusedText;

    protected PostItemController(CoursePageController parent) {
        super(parent);
    }

    protected void onItemChanged(PostResponse post) {
        postTitle.setText(post.getTitle());
        String substring = post.getDescription().length() > 100 ? post.getDescription().substring(0, 100) :
                post.getDescription();
        substring = substring.split("\\r?\\n")[0];
        descriptionText.setText(substring);
        doneText.setText("Gjort: " + post.getChoresDone() + "/" + post.getChoresCount());
        if (post.getChoresFocused() > 0) {
            focusedText.setText("(" + post.getChoresFocused() + " fokusert)");
        }
        else {
            focusedText.setText("");
        }

    }

    public static PostItemController load(CoursePageController parent) {
        return loadFromFXML("/scenes/postItem.fxml", new PostItemController(parent));
    }
}
