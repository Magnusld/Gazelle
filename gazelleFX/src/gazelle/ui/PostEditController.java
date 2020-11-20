package gazelle.ui;

import gazelle.api.ChoreResponse;
import gazelle.api.NewChoreRequest;
import gazelle.api.NewPostRequest;
import gazelle.api.PostContentResponse;
import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostEditController extends BaseController {

    @FXML
    private Text titleText;
    @FXML
    private TextField title;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private VBox choresContainer;
    @FXML
    private Button addChore;
    @FXML
    private Button savePost;
    @FXML
    private Button cancel;

    private final GazelleController app;
    private Long postId;
    private Long courseId;

    private final List<ChoreEditController> choreEdits;

    private PostEditController(GazelleController app) {
        this.app = app;
        this.choreEdits = new ArrayList<>();
    }

    private void setFreeze(boolean freeze) {
        addChore.setDisable(freeze);
        savePost.setDisable(freeze);
        cancel.setDisable(freeze);
    }

    private void clearFields() {
        title.setText("");
        description.setText("");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());

        choreEdits.clear();
        choresContainer.getChildren().clear();
    }

    private void setPostContent(PostContentResponse post) {
        this.courseId = post.getCourseId();
        title.setText(post.getTitle());
        description.setText(post.getDescription());
        startDate.setValue(post.getStartDate());
        endDate.setValue(post.getEndDate());

        assert choreEdits.isEmpty();
        for (ChoreResponse chore : post.getChores()) {
            ChoreEditController cec = ChoreEditController.load(this);
            cec.setChore(chore);
            choreEdits.add(cec);
        }

        choreEdits.sort((a, b) -> (int) (b.getKey() - a.getKey()));
        for (ChoreEditController cec : choreEdits)
            choresContainer.getChildren().add(cec.getNode());
    }

    public void onEditExistingPost(Long postId) {
        titleText.setText("Rediger innlegg");
        this.courseId = null;
        this.postId = postId;
        clearFields();
        setFreeze(true);
        app.sideRun(() -> {
            try {
                PostContentResponse post = app.getClient().posts().getPostContent(postId);
                app.mainRun(() -> {
                    setPostContent(post);
                    setFreeze(false);
                });
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke laste inn innlegg", e.getMessage());
                    app.showPostScreen(postId);
                });
            }
        });
    }

    public void onEditNewPost(Long courseId) {
        titleText.setText("Nytt innlegg");
        this.courseId = courseId;
        this.postId = null;
        clearFields();
        setFreeze(false);
    }

    @FXML
    public void onAddChoreButton() {
        ChoreEditController cec = ChoreEditController.load(this);
        long newKey = 0;
        if (choreEdits.size() > 0)
            newKey = choreEdits.get(choreEdits.size() - 1).getKey();
        cec.newWithKey(newKey);
        choreEdits.add(cec);
        choresContainer.getChildren().add(cec.getNode());
    }

    public void onChoreDelete(ChoreEditController cec) {
        choreEdits.remove(cec);
        choresContainer.getChildren().remove(cec.getNode());
    }

    @FXML
    public void onCancelButton() {
        if (this.postId != null)
            app.showPostScreen(postId);
        else
            app.showCourseScreen(courseId);
    }

    @FXML
    public void onSavePostButton() {
        NewPostRequest.Builder builder = new NewPostRequest.Builder();
        builder.title(title.getText())
                .description(description.getText())
                .startDate(startDate.getValue())
                .endDate(endDate.getValue());

        List<NewChoreRequest> chores = new ArrayList<>();
        for (ChoreEditController cec : choreEdits)
            chores.add(cec.makeNewChoreRequest());

        builder.chores(chores);
        NewPostRequest newPostRequest = builder.build();

        setFreeze(true);
        app.sideRun(() -> {
            try {
                if (postId != null)
                    app.getClient().posts().updateExistingPost(postId, newPostRequest);
                else {
                    postId = app.getClient().posts().addNewPost(courseId, newPostRequest).getId();
                }
                app.mainRun(() -> app.showPostScreen(postId));
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke lagre innlegg", e.getMessage());
                    setFreeze(false);
                });
            }
        });
    }

    public static PostEditController load(GazelleController app) {
        return loadFromFXML("/scenes/postEdit.fxml", new PostEditController(app));
    }
}
