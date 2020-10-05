package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends GazelleException {

    public CourseNotFoundException() {
        super("Course not found", null, HttpStatus.NOT_FOUND);
    }

}
