package gazelle.ui;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class FxUtils {
    public static void showAndWaitError(String title, String message) {
        Dialog<String> err = new Dialog<>();
        err.setTitle(title);
        err.setContentText(message);
        err.getDialogPane().getButtonTypes().add(
                new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE));
        err.showAndWait();
    }
}
