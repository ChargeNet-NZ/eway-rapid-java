package nz.net.charge.eway.rapid.sdk.exception;

import java.io.Serial;

/**
 * The root exception class for all business Rapid library API exceptions
 */
public abstract class RapidSdkException extends Exception {

    @Serial
    private static final long serialVersionUID = 7520026951213099151L;

    private String errorCode;

    public RapidSdkException(String errCode, String message) {
        super(message);
        this.errorCode = errCode;
    }

    public RapidSdkException(String errCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errCode;
    }

    /**
     * Get the business error code
     *
     * @return The business error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Set error code
     *
     * @param errorCode The business error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
