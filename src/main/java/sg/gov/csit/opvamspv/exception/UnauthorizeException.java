package sg.gov.csit.opvamspv.exception;

public class UnauthorizeException extends RuntimeException {

    public UnauthorizeException(String action) {
        super("You are not authorise to run: " + action);
    }
}
