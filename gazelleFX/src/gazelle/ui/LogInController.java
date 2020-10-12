package gazelle.ui;

import gazelle.client.error.LogInException;
import gazelle.client.error.SignUpException;
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
    private Button login;
    @FXML
    private Button signup;
    @FXML
    private Text errorText;

    private GazelleController app;

    private void onAction(boolean newUser) {
        final String username = this.username.getText();
        final String password = this.password.getText();

        errorText.setVisible(false);
        login.setDisable(true);
        signup.setDisable(true);

        app.sideRun(() -> {
            try {
                if (newUser)
                    app.getSession().signUp(username, password);
                else
                    app.getSession().logIn(username, password);
                app.mainRun(app::showMyCourses);
            } catch (SignUpException e) {
                app.mainRun(() -> {
                    switch (e.getReason()) {
                        case USERNAME_TAKEN -> errorText.setText("Brukernavnet er opptatt");
                        case PASSWORD_BAD -> errorText.setText("Passordet er for dårlig");
                        default -> errorText.setText(e.toString());
                    }
                    errorText.setVisible(true);
                });
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
                    signup.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void onLogInAction() {
        onAction(false);
    }

    @FXML
    private void onSignUpAction() {
        onAction(true);
    }

    public static LogInController load(GazelleController app) {
        LogInController controller = loadFromFXML("/scenes/login.fxml");
        controller.app = app;
        return controller;
    }
}
