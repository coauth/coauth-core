package dev.coauth.commons.exception;

public class NonFatalException  extends Exception{
    private final int errCode;

    public NonFatalException() {
        super();
        this.errCode = 0;
    }

    public NonFatalException(String message) {
        super(message);
        this.errCode = 0;
    }

    public NonFatalException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public NonFatalException(String message, Throwable cause) {
        super(message, cause);
        this.errCode = 0;
    }

    public NonFatalException(int errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return this.errCode;
    }
}
