package gazelle.ui;

import gazelle.api.PostResponse;
import gazelle.ui.list.ListItemController;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PostItemController extends ListItemController<PostResponse> {

    @FXML
    private Text postTitle;

    protected PostItemController(CoursePageController parent) {
        super(parent);
    }

    protected void onItemChanged(PostResponse post) {
        postTitle.setText(post.getTitle());
    }

    public static PostItemController load(CoursePageController parent) {
        return loadFromFXML("/scenes/postItem.fxml", new PostItemController(parent));
    }
}
