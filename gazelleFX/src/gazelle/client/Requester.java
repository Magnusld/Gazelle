package gazelle.client;

import gazelle.api.RestErrorObject;
import gazelle.client.error.ClientException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class Requester {

    private static ClientException makeError(Response response) {
        try {
            RestErrorObject obj = response.readEntity(RestErrorObject.class);
            return new ClientException(obj.getMessage());
        } catch (Exception e) {
            return new ClientException("failed to decode error", response);
        }
    }

    public static <T> T get(Invocation.Builder builder, Class<T> responseClass) {
        Response response = builder.get();
        if (!ResponseCode.successfulGet(response.getStatusInfo()))
            throw makeError(response);
        return response.readEntity(responseClass);
    }

    public static <T> T get(Invocation.Builder builder, GenericType<T> responseClass) {
        Response response = builder.get();
        if (!ResponseCode.successfulGet(response.getStatusInfo()))
            throw makeError(response);
        return response.readEntity(responseClass);
    }

    public static <T> void post(Invocation.Builder builder, T body) {
        Response response = builder.post(Entity.json(body));
        if (!ResponseCode.successfulPost(response.getStatusInfo()))
            throw makeError(response);
    }

    public static <T, U> T postWithResponse(Invocation.Builder builder,
                                            U body, Class<T> responseClass) {
        Response response = builder.post(Entity.json(body));
        if (!ResponseCode.successfulPostWithEntity(response.getStatusInfo()))
            throw makeError(response);
        return response.readEntity(responseClass);
    }

    public static <T, U> T postWithResponse(Invocation.Builder builder,
                                            U body, GenericType<T> responseClass) {
        Response response = builder.post(Entity.json(body));
        if (!ResponseCode.successfulPostWithEntity(response.getStatusInfo()))
            throw makeError(response);
        return response.readEntity(responseClass);
    }

    public static <T> void put(Invocation.Builder builder, T body) {
        Response response = builder.put(Entity.json(body));
        if (!ResponseCode.successfulPut(response.getStatusInfo()))
            throw makeError(response);
    }

    public static void delete(Invocation.Builder builder) {
        Response response = builder.delete();
        if (!ResponseCode.successfulDelete(response.getStatusInfo()))
            throw makeError(response);
    }
}
