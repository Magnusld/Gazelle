package gazelle.client;

public class SignUpException extends RuntimeException {
    public enum Reason {
        USERNAME_TAKEN,
        PASSWORD_BAD
    }

    private final Reason reason;
    public SignUpException(Reason reason) {
        super(reason.name());
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
