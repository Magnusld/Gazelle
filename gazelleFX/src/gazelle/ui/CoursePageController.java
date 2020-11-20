package gazelle.ui;

import gazelle.api.PostResponse;
import gazelle.client.error.ClientException;
import gazelle.ui.list.ListController;

import java.util.List;

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
            /*Long userId = app.getClient().loggedInUserId();
            List<PostResponse> courses =
                    owner ? app.getClient().courses().getOwnedCourses(userId)
                            : app.getClient().courses().getFollowedCourses(userId);
            app.mainRun(() -> setItems(courses));*/
            setOwnerMode(true);
            setTitle("Test");
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
                for (PostResponse post : toDelete);
                    //app.getClient().courses().deleteCourse(course.getId());
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
