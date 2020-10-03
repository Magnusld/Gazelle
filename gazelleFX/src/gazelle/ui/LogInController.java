package gazelle.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LogInController extends BaseController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private void onLogInAction() {

    }

    public static LogInController load() {
        return loadFromFXML("/scenes/login.fxml");
    }
}
