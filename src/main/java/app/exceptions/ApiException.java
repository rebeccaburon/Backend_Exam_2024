package app.exceptions;

public class ApiException extends RuntimeException{

    private final int statusCode;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCodeForAPI() {
        return statusCode;
    }




}