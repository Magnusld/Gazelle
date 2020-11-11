package gazelle.server.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExceptionHandlerTest {

    @Autowired
    private RestExceptionHandler exceptionHandler;

    private static final String MESSAGE = "test - Message";

    @Test
    public void testRestErrorObject() {
        GazelleException e = assertThrows(GazelleException.class, () -> {
            throw new GazelleException(MESSAGE, HttpStatus.CONFLICT);
        });
        ResponseEntity<Object> response = exceptionHandler.handleGazelleException(e, null);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        RestErrorObject reo = (RestErrorObject) response.getBody();
        assertNotNull(reo);
        assertEquals(MESSAGE, reo.getMessage());
    }
}
