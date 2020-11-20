package gazelle.ui.list;

import gazelle.ui.BaseController;
import javafx.fxml.FXML;

public abstract class ListItemController<U> extends BaseController {

    private final ListController<? extends ListItemController<U>, U> parent;

    private U item;

    private boolean deleteEnabled = false;
    private boolean markedForDelete = false;

    protected ListItemController(ListController<? extends ListItemController<U>, U> parent) {
        this.parent = parent;
    }

    @FXML
    public void handleClick() {
        if (deleteEnabled) {
            setMarkedForDelete(!markedForDelete);
        } else {
            parent.onItemSelected(item);
        }
    }

    public void setDeleteEnabled(boolean deleteEnabled) {
        this.deleteEnabled = deleteEnabled;
        setMarkedForDelete(false);
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
        if (markedForDelete)
            getNode().getStyleClass().add("markedForDelete");
        else
            getNode().getStyleClass().remove("markedForDelete");
        parent.onDeleteMarkChanged();
    }

    public boolean markedForDelete() {
        return this.markedForDelete;
    }

    public void setItem(U item) {
        this.item = item;
        this.setDeleteEnabled(false);
        onItemChanged(item);
    }

    public U getItem() {
        return item;
    }

    protected abstract void onItemChanged(U item);
}
