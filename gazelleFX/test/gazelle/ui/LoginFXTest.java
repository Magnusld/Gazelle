package gazelle.ui;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

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
        mockServer = ClientAndServer.startClientAndServer(8088);
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
        /*
        clickOn("#username").write("food");
        clickOn("#password").write("barf");
        clickOn("#login");
        Thread.sleep(5000);
        UserResponse user = app.getClient().session().getLoggedInUser();
        assertEquals(user.getUsername(), "food");
        */
    }

    @Test
    public void newCourseTest() {

    }

    @Override
    public void stop() {
        mockServer.stop();
    }
}