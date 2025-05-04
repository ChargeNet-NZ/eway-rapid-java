package nz.net.charge.eway.rapid.sdk.exception;

import nz.net.charge.eway.rapid.sdk.util.Constant;

import java.io.Serial;

public class CommunicationFailureException extends RapidSdkException {

    @Serial
    private static final long serialVersionUID = 8857227165097343287L;

    public CommunicationFailureException(String message, Throwable t) {
        super(Constant.COMMUNICATION_FAILURE_ERROR_CODE, message, t);
    }

}
