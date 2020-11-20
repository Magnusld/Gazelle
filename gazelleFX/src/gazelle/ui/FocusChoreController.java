package gazelle.ui;

import gazelle.api.ChoreFullResponse;
import gazelle.api.ChoreResponse;
import gazelle.api.ValueWrapper;
import gazelle.client.error.ClientException;
import gazelle.model.UserChoreProgress;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

public class FocusChoreController extends BaseController {

    @FXML
    private Text choreLocation;
    @FXML
    private CheckBox doneBox;
    @FXML
    private Text text;
    @FXML
    private CheckBox focusCheck;

    private final GazelleController app;

    private Long choreId;

    private FocusChoreController(GazelleController app) {
        this.app = app;
    }

    public void setChore(ChoreFullResponse chore) {
        this.choreId = chore.getId();
        text.setText(chore.getText());
        choreLocation.setText(chore.getCourseName() + ">" + chore.getPostTitle());
        setState(chore.getProgress());
    }

    private void setState(UserChoreProgress.Progress progress) {
        this.doneBox.setSelected(progress == UserChoreProgress.Progress.DONE);
        this.focusCheck.setSelected(progress == UserChoreProgress.Progress.FOCUSED);
        this.focusCheck.setVisible(progress != UserChoreProgress.Progress.DONE);

        app.sideRun(() -> {
            try {
                Long userId = app.getClient().loggedInUserId();
                app.getClient().chores().setChoreState(choreId, userId, progress);
            } catch (ClientException e) {
                FxUtils.showAndWaitError("Klarte ikke oppdatere oppgave", e.getMessage());
                app.mainRun(app::showMyCourses);
            }
        });
    }

    @FXML
    public void onDoneChange() {
        setState(doneBox.isSelected()
                ? UserChoreProgress.Progress.DONE
                : UserChoreProgress.Progress.FOCUSED);
    }

    @FXML
    public void onFocusCheckChange() {
        setState(focusCheck.isSelected()
                ? UserChoreProgress.Progress.FOCUSED
                : UserChoreProgress.Progress.UNDONE);
    }

    public static FocusChoreController load(GazelleController app) {
        return loadFromFXML("/scenes/focusChore.fxml", new FocusChoreController(app));
    }
}