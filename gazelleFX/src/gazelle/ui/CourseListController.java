package gazelle.ui;

import gazelle.api.CourseResponse;
import gazelle.api.NewCourseRequest;
import gazelle.api.UserResponse;
import gazelle.client.error.ClientException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseListController extends BaseController {

    @FXML
    private Text title;
    @FXML
    private Button newCourse;
    @FXML
    private Button deleteCourse;
    @FXML
    private VBox courseList;
    private final ArrayList<CourseItemController> controllers = new ArrayList<>();

    private GazelleController app;
    private boolean isDeleting = false;
    private boolean ownedCoursesMode = false;

    @FXML
    private void initialize() throws IOException {}

    public void onShow(boolean ownedCoursesMode) {
        this.ownedCoursesMode = ownedCoursesMode;

        title.setText(ownedCoursesMode ? "Mine løp" : "Fulgte løp");

        newCourse.setVisible(ownedCoursesMode);
        if (!ownedCoursesMode)
            deleteCourse.setVisible(false);

        setIsDeleting(false);
        setFreeze(false);

        clearView();
        app.sideRun(() -> {
            Long userId = app.getClient().loggedInUserId();
            List<CourseResponse> courses =
                    ownedCoursesMode ? app.getClient().courses().getOwnedCourses(userId)
                            : app.getClient().courses().getFollowedCourses(userId);
            app.mainRun(() -> setCourses(courses));
        });
    }

    private void setFreeze(boolean freeze) {
        newCourse.setDisable(freeze);
        deleteCourse.setDisable(freeze);
    }

    private void clearView() {
        courseList.setVisible(false); //TODO: Add spinner
    }

    private void setCourses(List<CourseResponse> courses) {
        // Make enough controllers
        while (controllers.size() < courses.size())
            controllers.add(CourseItemController.load(this));

        // Remove extra controllers
        controllers.subList(courses.size(), controllers.size()).clear();

        // Assign a course per controller
        for (int i = 0; i < courses.size(); i++)
            controllers.get(i).setCourse(courses.get(i));

        // Set list content to the controllers
        courseList
                .getChildren()
                .setAll(controllers.stream()
                        .map(BaseController::getNode)
                        .collect(Collectors.toList()));

        courseList.setVisible(true);
        deleteCourse.setVisible(ownedCoursesMode && !courses.isEmpty());
    }

    @FXML
    public void handleNewCourseClick() {
        assert (ownedCoursesMode);
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

        app.sideRun(() -> {
            try {
                NewCourseRequest newCourseRequest = new NewCourseRequest(name);
                app.getClient().courses().addNewCourse(newCourseRequest);
                app.mainRun(() -> onShow(ownedCoursesMode));
            } catch (ClientException e) {
                app.mainRun(() -> {
                    FxUtils.showAndWaitError("Klarte ikke lage løp", e.getMessage());
                });
            }
        });
    }

    public void onCourseSelected(CourseResponse course) {
        app.showCourseScreen(course.getId());
    }

    @FXML
    public void handleDeleteCourseClick() {
        assert (ownedCoursesMode);
        if (this.isDeleting) {
            ArrayList<Long> toDelete = new ArrayList<>();
            for (CourseItemController cic : controllers)
                if (cic.markedForDelete())
                    toDelete.add(cic.getCourse().getId());
            if (toDelete.size() > 0) {
                setFreeze(true);
                app.sideRun(() -> {
                    try {
                        for (Long id : toDelete)
                            app.getClient().courses().deleteCourse(id);
                    } catch (ClientException e) {
                        FxUtils.showAndWaitError("Klarte ikke slette løp", e.getMessage());
                    } finally {
                        app.mainRun(() -> onShow(ownedCoursesMode));
                    }
                });
                return;
            }
        }
        setIsDeleting(!this.isDeleting);
    }

    private void setIsDeleting(boolean isDeleting) {
        this.isDeleting = isDeleting;
        if (isDeleting) {
            onDeleteMarkChanged();
            getNode().getStyleClass().add("deleteMode");
        } else {
            deleteCourse.setText(null);
            getNode().getStyleClass().remove("deleteMode");
        }
        for (CourseItemController cic : controllers)
            cic.setDeleteEnabled(isDeleting);
    }

    public void onDeleteMarkChanged() {
        int count = 0;
        for (CourseItemController cic : controllers) {
            if (cic.markedForDelete())
                count++;
        }
        deleteCourse.setText(String.format("Slett %d løp", count));
    }

    public static CourseListController load(GazelleController app) {
        CourseListController controller = loadFromFXML("/scenes/courseList.fxml");
        controller.app = app;
        return controller;
    }
}
