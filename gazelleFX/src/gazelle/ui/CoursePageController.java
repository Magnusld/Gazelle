package gazelle.ui;

import gazelle.api.CourseContentResponse;
import gazelle.api.PostResponse;
import gazelle.client.error.ClientException;
import gazelle.ui.list.ListController;

import java.util.List;
import java.util.Objects;

public class CoursePageController extends ListController<PostItemController, PostResponse> {

    private final GazelleController app;

    private Long courseId;

    private CoursePageController(GazelleController app) {
        this.app = app;
    }

    public void onShow(Long courseId) {
        this.courseId = courseId;
        super.onFirstShow();
    }

    protected String getNewButtonText() {
        return "Nytt innlegg";
    }

    protected String getDeleteText(int count) {
        return String.format("Slett %d innlegg", count);
    }

    protected PostItemController makeNewItemController() {
        return PostItemController.load(this);
    }

    protected void requestListFill(boolean full) {
        app.sideRun(() -> {
            try {
                CourseContentResponse course = app.getClient().courses().findById(courseId);
                Boolean isOwner = course.getIsOwner();
                Objects.requireNonNull(isOwner);

                app.mainRun(() -> {
                    setOwnerMode(isOwner);
                    setTitle(course.getName());
                    setItems(course.getPosts());
                });
            } catch (ClientException e) {
                FxUtils.showAndWaitError("Klarte ikke hente løpet", e.getMessage());
                app.mainRun(app::showMyCourses);
            }
        });
    }

    protected void onItemSelected(PostResponse post) {
        //app.showPostScreen(post.getId());
    }

    protected void onNewButtonPressed() {
        //app.showNewPostScreen(courseId);
    }

    protected void doDeletes(List<PostResponse> toDelete) {
        app.sideRun(() -> {
            try {
                for (PostResponse post : toDelete) {
                    app.getClient().posts().deletePost(post.getId());
                }
            } catch (ClientException e) {
                FxUtils.showAndWaitError("Klarte ikke slette løp", e.getMessage());
            } finally {
                app.mainRun(this::refresh);
            }
        });
    }

    public static CoursePageController load(GazelleController app) {
        return loadFromFXML("/scenes/list.fxml", new CoursePageController(app));
    }
}
