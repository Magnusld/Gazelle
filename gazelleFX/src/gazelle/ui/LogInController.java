package gazelle.ui;

import gazelle.client.error.LogInException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LogInController extends BaseController {

    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private Button login;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private Text errorText;

    private GazelleController app;

    @FXML
    private void initialize() {
        email.textProperty().addListener(o -> onTextFieldUpdated());
        password.textProperty().addListener(o -> onTextFieldUpdated());
    }

    public void onShow() {
        email.setText("");
        password.setText("");
        login.setDisable(false);
        signUpLink.setDisable(false);
        errorText.setVisible(false);
    }
    
    private void onTextFieldUpdated() {
        errorText.setVisible(false);
    }

    @FXML
    private void onLogInAction() {
        final String email = this.email.getText();
        final String password = this.password.getText();

        errorText.setVisible(false);
        login.setDisable(true);
        signUpLink.setDisable(true);

        app.sideRun(() -> {
            try {
                app.getClient().login().logIn(email, password);
                app.mainRun(app::showMyCourses);
            } catch (LogInException e) {
                app.mainRun(() -> {
                    errorText.setText("Brukernavn/passord er feil");
                    errorText.setVisible(true);
                });
            } catch (Exception e) {
                app.mainRun(() -> {
                    errorText.setText(e.toString());
                    errorText.setVisible(true);
                });
                throw e;
            } finally {
                app.mainRun(() -> {
                    login.setDisable(false);
                    signUpLink.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void onSignUpLink() {
        app.showSignUpScreen();
    }

    public static LogInController load(GazelleController app) {
        LogInController controller = loadFromFXML("/scenes/login.fxml");
        controller.app = app;
        return controller;
    }
}
