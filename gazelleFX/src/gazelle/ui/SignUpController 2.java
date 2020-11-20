package gazelle.ui;

import gazelle.client.error.SignUpException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SignUpController extends BaseController {

    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField password2;
    @FXML
    private Button signup;
    @FXML
    private Hyperlink logInLink;
    @FXML
    private Text errorText;

    private GazelleController app;

    @FXML
    private void initialize() {
        firstname.textProperty().addListener(o -> onTextFieldUpdated());
        lastname.textProperty().addListener(o -> onTextFieldUpdated());
        email.textProperty().addListener(o -> onTextFieldUpdated());
        password.textProperty().addListener(o -> onTextFieldUpdated());
        password2.textProperty().addListener(o -> onTextFieldUpdated());
    }

    public void onShow() {
        firstname.setText("");
        lastname.setText("");
        email.setText("");
        password.setText("");
        password2.setText("");
        signup.setDisable(false);
        logInLink.setDisable(false);
        errorText.setVisible(false);
    }


    private void onTextFieldUpdated() {
        errorText.setVisible(false);
    }

    @FXML
    private void onSignUpAction() {
        final String firstname = this.firstname.getText();
        final String lastname = this.lastname.getText();
        final String email = this.email.getText();
        final String password = this.password.getText();
        final String password2 = this.password2.getText();

        if (!password.equals(password2)) {
            errorText.setText("Passordene stemmer ikke overens");
            errorText.setVisible(true);
            return;
        }

        errorText.setVisible(false);
        signup.setDisable(true);
        logInLink.setDisable(true);

        app.sideRun(() -> {
            try {
                app.getClient().login().signUp(firstname, lastname, email, password);
                app.mainRun(app::showMyCourses);
            } catch (SignUpException e) {
                app.mainRun(() -> {
                    errorText.setText(e.getMessage());
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
                    signup.setDisable(false);
                    logInLink.setDisable(false);
                });
            }
        });
    }

    @FXML
    private void onLogInLink() {
        app.showLogInScreen();
    }

    public static SignUpController load(GazelleController app) {
        SignUpController controller = loadFromFXML("/scenes/signup.fxml");
        controller.app = app;
        return controller;
    }
}
