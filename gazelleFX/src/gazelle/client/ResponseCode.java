package gazelle.client;

import javax.ws.rs.core.Response;

public class ResponseCode {
    /**
     * Checks if the status code corresponds with a successful GET request.
     * Only 200 (OK) is seen as success.
     * @param status the response status
     * @return true if the response was successful
     */
    public static boolean successfulGet(Response.StatusType status) {
        return status.equals(Response.Status.OK);
    }

    /**
     * Checks if the status code corresponds with a successful POST request.
     * 201 (Created) means the URI for the created object is in the Location header.
     * 200 (OK) and 204 (No content) mean the object was created, and in the first case, returned.
     * @param status the response status
     * @return true if the response was successful
     */
    public static boolean successfulPost(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.CREATED)
                || status.equals(Response.Status.NO_CONTENT);
    }

    /**
     * Checks if the status code corresponds with a successful POST request with
     * the newly created object included in the body of the response.
     * @param status the response status
     * @return true if the status indicates that the created object is in the response body
     */
    public static boolean successfulPostWithEntity(Response.StatusType status) {
        return status.equals(Response.Status.OK);
    }

    /**
     * Checks if the status code corresponds with a successful PUT request.
     * If the object is created the status is 201 (Created).
     * If the object already existed and is replaced,
     *   the status code is 200 (OK) or 204 (No content).
     * @param status the response status
     * @return true if the response was successful
     */
    public static boolean successfulPut(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.CREATED)
                || status.equals(Response.Status.NO_CONTENT);
    }

    /**
     * Checks if the status code corresponds with a successful DELETE request.
     * 200 (OK) means the object is deleted and the response contains an entity.
     * 202 (Accepted) means the object is queued for eventual deletion.
     * 204 (No Content) means the object is deleted but no other info is returned.
     * @param status the response status
     * @return true if the response was successful
     */
    public static boolean successfulDelete(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.ACCEPTED)
                || status.equals(Response.Status.NO_CONTENT);
    }
}
