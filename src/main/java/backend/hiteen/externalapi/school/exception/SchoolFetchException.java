package backend.hiteen.externalapi.school.exception;

public class SchoolFetchException extends RuntimeException {
    public SchoolFetchException(String message) {
        super(message);
    }

    public SchoolFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
