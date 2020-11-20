package gazelle.ui;

import gazelle.api.ChoreResponse;
import gazelle.api.PostContentResponse;
import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PostPageController extends BaseController {

    @FXML
    private Text title;
    @FXML
    private Text description;
    @FXML
    private VBox list;
    @FXML
    private Button editButton;

    private final GazelleController app;
    private Long postId;

    private PostPageController(GazelleController app) {
        this.app = app;
    }

    public void onShow(Long postId) {
        this.postId = postId;
        title.setText("");
        description.setText("");
        editButton.setVisible(false);
        list.getChildren().clear();

        app.sideRun(() -> {
            try {
                PostContentResponse post = app.getClient().posts().getPostContent(postId);
                app.mainRun(() -> {
                    title.setText(post.getTitle());
                    description.setText(post.getDescription());
                    Boolean isOwner = post.getIsOwning();
                    editButton.setVisible(isOwner != null && isOwner);

                    for (ChoreResponse chore : post.getChores()) {
                        ChoreController cc = ChoreController.load(app);
                        cc.setChore(chore);
                        list.getChildren().add(cc.getNode());
                    }
                });
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke laste inn innlegg", e.getMessage());
                    app.showMyCourses();
                });
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
