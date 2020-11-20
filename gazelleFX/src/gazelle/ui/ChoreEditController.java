package gazelle.ui;

import gazelle.api.ChoreResponse;
import gazelle.api.NewChoreRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

public class ChoreEditController extends BaseController {

    @FXML
    private TextField choreText;
    @FXML
    private Button deleteButton;

    private final PostEditController parent;

    private @Nullable Long choreId;
    private long key;

    private ChoreEditController(PostEditController parent) {
        this.parent = parent;
    }

    public void newWithKey(long key) {
        this.key = key;
        choreText.setText("");
    }

    public void setChore(ChoreResponse chore) {
        this.choreId = chore.getId();
        this.key = chore.getKey();
        choreText.setText(chore.getText());
    }

    public long getKey() {
        return key;
    }

    public NewChoreRequest makeNewChoreRequest() {
        return new NewChoreRequest(choreId, key, choreText.getText(), null);
    }

    @FXML
    public void onDeleteButtonClick() {
        parent.onChoreDelete(this);
    }

    public static ChoreEditController load(PostEditController parent) {
        return loadFromFXML("/scenes/choreEdit.fxml", new ChoreEditController(parent));
    }
}
