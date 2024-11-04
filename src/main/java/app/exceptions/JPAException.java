package app.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAException extends RuntimeException {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(JPAException.class);
    private final int statusCode;

    public JPAException(int statusCode, String message) {
        super(message);
        writeToLog(message);
        this.statusCode = statusCode;
    }

    private void writeToLog(String message) {
        logger.error(message);
    }
    public int getStatusCodeForJPA() {
        return statusCode;
    }
}