package gazelle.ui;

import gazelle.api.PostContentResponse;
import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PostPageController extends BaseController {

    @FXML
    private Text title;
    @FXML
    private Text description;
    @FXML
    private VBox list;

    private final GazelleController app;
    private Long postId;

    private PostPageController(GazelleController app) {
        this.app = app;
    }

    public void onShow(Long postId) {
        this.postId = postId;
        title.setText("");
        description.setText("");
        list.getChildren().clear();

        app.sideRun(() -> {
            try {
                PostContentResponse post = app.getClient().posts().getPostContent(postId);
                app.mainRun(() -> {
                    title.setText(post.getTitle());
                    description.setText(post.getDescription());


                });
            } catch (ClientException e) {
                FxUtils.showAndWaitError("Klarte ikke laste inn innlegg", e.getMessage());
                app.mainRun(app::showMyCourses);
            }
        });
    }

    @FXML
    public void onEditButton() {
        app.showPostEditScreen(postId);
    }

    public static PostPageController load(GazelleController app) {
        return loadFromFXML("/scenes/postPage.fxml", new PostPageController(app));
    }
}
