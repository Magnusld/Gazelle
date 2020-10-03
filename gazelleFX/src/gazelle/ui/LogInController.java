package gazelle.ui;

import gazelle.client.ClientException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LogInController extends BaseController {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button logIn;
    @FXML
    private Text errorText;

    private GazelleController app;

    @FXML
    private void onLogInAction() {
        String username = this.username.getText();
        String password = this.password.getText();

        errorText.setVisible(false);
        logIn.setDisable(true);
        app.sideRun(() -> {
            try {
                app.getSession().logIn(username, password);
                app.mainRun(app::showMyCourses);
            } catch(Exception e) {
                app.mainRun(() -> {
                    errorText.setText(e.toString());
                    errorText.setVisible(true);
                });
                throw e;
            } finally {
                app.mainRun(() -> logIn.setDisable(false));
            }
        });
    }

    public static LogInController load(GazelleController app) {
        LogInController controller = loadFromFXML("/scenes/login.fxml");
        controller.app = app;
        return controller;
    }
}
