package gazelle.ui;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.beans.Transient;
import java.util.concurrent.TimeUnit;

import gazelle.api.UserResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Her må vi sette opp ein MockServer fordi det meste av funksjonalitet
 * berre fungerer dersom vi har ein server.
 * Inspirasjon frå https://www.baeldung.com/mockserver
 */
public class LoginFXTest extends ApplicationTest {

    private ClientAndServer mockServer = ClientAndServer.startClientAndServer(8055);

    private Parent parent;
    private GazelleController app;


    @Override
    public void start(final Stage stage) throws Exception {
        createExpectationForLogin();
        createCoursesForUser();
        createExpectationForSignUp();

        app = GazelleController.load();
        app.getClient().setBaseURL("http://localhost:8055");
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
                                        + "{ \"id\":8, \"firstName\": \"food\", \"lastName\": \"test\"}}}")
                                .withDelay(TimeUnit.MILLISECONDS, 50));
    }

    private void createExpectationForSignUp() {
        mockServer
                .when(
                        request()
                                .withMethod("POST")
                                .withPath("/signup"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("{\"token\": \"dummy-token\", \"user\": "
                                        + "{ \"id\":8, \"firstName\": \"food\", \"lastName\": \"test\"}}}")
                                .withDelay(TimeUnit.MILLISECONDS, 50));
    }

    private static final String CHORERESPONSE = "{\"id\": 1, \"key\": 1, \"text\": \"test\"," +
            "\"dueDate\": \"2020-10-10\", \"progress\": null}";
    private static final String POSTRESPONSE = "{\"id\": 1," +
            "\"title\": \"dummy\", \"description\": \"test\"," +
            "\"startDate\": \"2020-10-10\", \"endDate\": \"2020-10-10\", \"nextChoreDue\": \" " + CHORERESPONSE + "," +
            "\"choresDone\": null, \"choresFocused\": null, \"choresCount\": 1}";
    private static final String COURSERESPONSE = "{\"id\": 1, \"name\": \"dummy\", \"isOwner\": true," +
                                        "\"isFollower\": true, \"currentPost\": " + POSTRESPONSE + "," +
            "\"nextPost\": null, \"previousPost\": null, \"nextChoreDue\": " + CHORERESPONSE + "}";
    private void createCoursesForUser() {
        mockServer
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/users/5/courses"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeaders(
                                        new Header("Content-Type", "application/json"))
                                .withBody("[" + COURSERESPONSE + "]")
                                .withDelay(TimeUnit.MILLISECONDS, 50));
    }

    @Test
    public void loginTest() throws InterruptedException {
        clickOn("#email").write("test@test.no");
        clickOn("#password").write("barf");
        clickOn("#login");
        Thread.sleep(5000);
        UserResponse user = app.getClient().session().getLoggedInUser();
        assertTrue(user.getFirstName().equals("food"));
    }

    @Test
    public void signUpTest() throws InterruptedException {
        clickOn("#signUpLink");
        clickOn("#firstname").write("food");
        clickOn("#lastname").write("test");
        clickOn("#email").write("test@test.no");
        clickOn("#password").write("barf");
        clickOn("#password2").write("barf");
        clickOn("#signup");
        Thread.sleep(5000);
        UserResponse user = app.getClient().session().getLoggedInUser();
        assertTrue(user.getFirstName().equals("food"));
    }

    private UserResponse makeUserResponse() {
        UserResponse.Builder builder = new UserResponse.Builder();
        builder.firstName("Per").lastName("Johhny").id(5L);
        return builder.build();
    }

    @Test
    public void courseTest() throws InterruptedException {
        app.getClient().session().setLoggedIn("dommy-token", makeUserResponse());
        createCoursesForUser();
        Platform.runLater(app::showMyCourses);
        Thread.sleep(3000);
        Platform.runLater(app::showFollowedCourses);
        Thread.sleep(3000);
        Platform.runLater(app::showFocusList);
        Thread.sleep(3000);
        Platform.runLater(() -> app.showPostScreen(1L));
        Thread.sleep(3000);
        Platform.runLater(() -> app.showPostEditScreen(1L));
        Thread.sleep(3000);
        Platform.runLater(() -> app.showNewPostScreen(1L));
        Thread.sleep(3000);
    }

    @Override
    public void stop() {
        mockServer.stop();
    }
}
