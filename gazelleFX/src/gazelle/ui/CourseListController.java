package gazelle.ui;

import gazelle.api.CourseResponse;
import gazelle.api.NewCourseRequest;
import gazelle.client.error.ClientException;
import gazelle.ui.list.ListController;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class CourseListController extends ListController<CourseItemController, CourseResponse> {

    private final GazelleController app;

    private boolean ownerMode;

    private CourseListController(GazelleController app) {
        this.app = app;
    }

    public void onShow(boolean ownerMode) {
        this.ownerMode = ownerMode;
        super.onFirstShow();
        super.setTitle(ownerMode ? "Mine løp" : "Fulgte løp");
        super.setOwnerMode(ownerMode);
    }

    protected String getNewButtonText() {
        return "Nytt løp";
    }

    protected String getDeleteText(int count) {
        return String.format("Slett %d løp", count);
    }

    protected CourseItemController makeNewItemController() {
        return CourseItemController.load(this);
    }

    protected void requestListFill(boolean full) {
        app.sideRun(() -> {
            try {
                Long userId = app.getClient().loggedInUserId();
                List<CourseResponse> courses =
                        ownerMode ? app.getClient().courses().getOwnedCourses(userId)
                                : app.getClient().courses().getFollowedCourses(userId);
                app.mainRun(() -> {
                    setItems(courses);
                });
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke hente løpsliste", e.getMessage());
                    app.showLogInScreen();
                });
            }
        });
    }

    protected void onItemSelected(CourseResponse course) {
        app.showCourseScreen(course.getId());
    }

    protected void onNewButtonPressed() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nytt løp");
        dialog.setContentText("Navn");
        dialog.setHeaderText("Lag et nytt løp");
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty())
            return;
        String name = result.get();
        if (name.isBlank())
            return;

        setFreeze(true);
        app.sideRun(() -> {
            try {
                NewCourseRequest newCourseRequest = new NewCourseRequest(name);
                app.getClient().courses().addNewCourse(newCourseRequest);
                app.mainRun(this::refresh);
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke lage løp", e.getMessage());
                    setFreeze(false);
                });
            }
        });
    }

    protected void doDeletes(List<CourseResponse> toDelete) {
        app.sideRun(() -> {
            try {
                for (CourseResponse course : toDelete)
                    app.getClient().courses().deleteCourse(course.getId());
                app.mainRun(this::refresh);
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke slette løp", e.getMessage());
                    refresh();
                });
            }
        });
    }

    public static CourseListController load(GazelleController app) {
        return loadFromFXML("/scenes/list.fxml", new CourseListController(app));
    }
}
