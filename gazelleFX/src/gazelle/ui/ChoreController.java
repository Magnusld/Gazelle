package gazelle.ui;

import gazelle.api.ChoreResponse;
import gazelle.client.error.ClientException;
import gazelle.model.UserChoreProgress;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

public class ChoreController extends BaseController {

    @FXML
    private CheckBox doneBox;
    @FXML
    private Text text;
    @FXML
    private CheckBox focusCheck;

    private final GazelleController app;

    private Long choreId;

    private ChoreController(GazelleController app) {
        this.app = app;
    }

    public void setChore(ChoreResponse chore) {
        this.choreId = chore.getId();
        text.setText(chore.getText());
        setState(chore.getProgress());
    }

    private void setState(UserChoreProgress.Progress progress) {
        this.doneBox.setSelected(progress == UserChoreProgress.Progress.DONE);
        this.focusCheck.setSelected(progress == UserChoreProgress.Progress.FOCUSED);
        this.focusCheck.setVisible(progress != UserChoreProgress.Progress.DONE);
        try {
            app.sideRun(() -> {
                app.getClient().
            });
        } catch (ClientException e) {

        }
    }

    @FXML
    public void onDoneChange() {

    }

    @FXML
    public void onFocusCheckChange() {

    }

    public ChoreController load(GazelleController app) {
        return loadFromFXML("scenes/chore.fxml", new ChoreController(app));
    }
}
