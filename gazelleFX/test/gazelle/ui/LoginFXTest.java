package gazelle.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpForward.forward;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

import gazelle.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpForward;
import org.mockserver.verify.VerificationTimes;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.concurrent.TimeUnit;

/**
 * Her må vi sette opp ein MockServer fordi det meste av funksjonalitet
 * berre fungerer dersom vi har ein server.
 * Inspirasjon frå https://www.baeldung.com/mockserver
 */
public class LoginFXTest extends ApplicationTest {

    private ClientAndServer mockServer;

    private Parent parent;
    private GazelleController app;


    @Override
    public void start(final Stage stage) throws Exception {
        mockServer = ClientAndServer.startClientAndServer(8080);
        createExpectationForLogin();
        createCoursesForUser();

        app = GazelleController.load();
        parent = app.getNode();
        stage.setScene(new Scene(parent));
        stage.show();
    }

    private void createExpectationForLogin() {
        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/login"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("{\"token\": \"dummy-token\", \"user\": "
                                        + "{ \"id\":8, \"username\": \"food\","
                                        + " \"password\":\"barf\" }}")
                                .withDelay(TimeUnit.MILLISECONDS, 50));
    }

    private void createCoursesForUser() {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/8/courses"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("[]")
                                .withDelay(TimeUnit.MILLISECONDS, 50));
    }

    @Test
    public void loginTest() throws InterruptedException {
        clickOn("#username").write("food");
        clickOn("#password").write("barf");
        clickOn("#login");
        Thread.sleep(5000);
        User user = app.getSession().getLoggedInUser();
        assertEquals(user.getUsername(), "food");
        assertEquals(user.getPassword(), "barf");
    }

    @Test
    public void newCourseTest() {
        //
    }

    @Override
    public void stop() {
        mockServer.stop();
    }
}
