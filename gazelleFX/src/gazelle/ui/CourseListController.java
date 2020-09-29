package gazelle.ui;

import gazelle.model.Course;
import gazelle.model.Database;
import gazelle.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseListController extends BaseController {

    @FXML
    public Button newCourse;
    @FXML
    private VBox courseList;
    private ArrayList<CourseController> controllers = new ArrayList<>();

    private Database database;
    private User user;

    @FXML
    private void initialize() throws IOException {

    }

    public void setData(Database database, User user) {
        this.database = database;
        this.user = user;
        updateCourses();
    }

    private void updateCourses() {
        List<Course> courses = database.getCoursesOwned(user);
        while (controllers.size() < courses.size())
            controllers.add(CourseController.load());
        controllers.subList(courses.size(), controllers.size()).clear(); // Remove extra controllers
        for (int i = 0; i < courses.size(); i++)
            controllers.get(i).setCourse(courses.get(i));
        courseList
                .getChildren()
                .setAll(controllers.stream().map(e -> e.getNode()).collect(Collectors.toList()));
    }

    @FXML
    public void handleNewCourseClick() {
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
        Course newCourse = database.newCourse(name);
        newCourse.addOwner(user);
        updateCourses();
    }

    public static CourseListController load() throws IOException {
        return loadFromFXML("/scenes/courseList.fxml");
    }
}
