package gazelle.ui.list;

import gazelle.ui.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ListController<T extends ListItemController<U>, U> extends BaseController {
    @FXML
    private Text title;
    @FXML
    private Button newButton;
    @FXML
    private Button deleteButton;
    @FXML
    private VBox list;

    private final ArrayList<T> controllers = new ArrayList<>();

    private boolean isDeleting = false;
    private boolean ownerMode = false;

    @FXML
    private void initialize() {
        newButton.setText(getNewButtonText());
    }

    public void onFirstShow() {
        newButton.setVisible(false);
        deleteButton.setVisible(false);
        title.setText("");
        setOwnerMode(false);
        setIsDeleting(false);
        setFreeze(false);
        clearView();
        requestListFill(true);
    }

    public void refresh() {
        setIsDeleting(false);
        clearView();
        requestListFill(false);
    }

    protected void setFreeze(boolean freeze) {
        newButton.setDisable(freeze);
        deleteButton.setDisable(freeze);
    }

    private void clearView() {
        list.setVisible(false);
    }

    protected void setTitle(String title) {
        this.title.setText(title);
    }

    protected void setOwnerMode(boolean ownerMode) {
        this.ownerMode = ownerMode;

        newButton.setVisible(ownerMode);
        if (!ownerMode)
            deleteButton.setVisible(false);
    }

    protected void setItems(List<U> items) {
        // Make enough controllers
        while (controllers.size() < items.size())
            controllers.add(makeNewItemController());

        // Remove extra controllers
        controllers.subList(items.size(), controllers.size()).clear();

        // Assign a course per controller
        for (int i = 0; i < items.size(); i++)
            controllers.get(i).setItem(items.get(i));

        // Set list content to the controllers
        list.getChildren().setAll(controllers.stream()
                        .map(BaseController::getNode)
                        .collect(Collectors.toList()));

        list.setVisible(true);
        deleteButton.setVisible(ownerMode && !items.isEmpty());
    }

    @FXML
    public void handleNewButtonClick() {
        assert ownerMode;
        onNewButtonPressed();
    }

    @FXML
    public void handleDeleteButtonClick() {
        assert (ownerMode);
        if (this.isDeleting) {
            ArrayList<U> toDelete = new ArrayList<>();
            for (T controller : controllers)
                if (controller.markedForDelete())
                    toDelete.add(controller.getItem());
            if (toDelete.size() > 0) {
                setFreeze(true);
                doDeletes(toDelete);
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
            deleteButton.setText(null);
            getNode().getStyleClass().remove("deleteMode");
        }
        for (T controller : controllers)
            controller.setDeleteEnabled(isDeleting);
    }

    public void onDeleteMarkChanged() {
        int count = 0;
        for (T cic : controllers) {
            if (cic.markedForDelete())
                count++;
        }
        deleteButton.setText(getDeleteText(count));
    }

    protected abstract String getNewButtonText();

    protected abstract String getDeleteText(int count);

    protected abstract T makeNewItemController();

    protected abstract void requestListFill(boolean full);

    protected abstract void onItemSelected(U item);

    protected abstract void onNewButtonPressed();

    protected abstract void doDeletes(List<U> toDelete);
}
