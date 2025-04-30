package nz.net.charge.eway.rapid.sdk.exception;

import nz.net.charge.eway.rapid.sdk.util.Constant;

import java.io.Serial;

public class SystemErrorException extends RapidSdkException {

    @Serial
    private static final long serialVersionUID = 852475063038180563L;

    public SystemErrorException(String message) {
        super(Constant.INTERNAL_RAPID_SERVER_ERROR_CODE, message);
    }

    public SystemErrorException(String message, Throwable t) {
        super(Constant.INTERNAL_RAPID_SERVER_ERROR_CODE, message, t);
    }

}
