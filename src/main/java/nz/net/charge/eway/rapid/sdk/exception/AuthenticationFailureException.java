package nz.net.charge.eway.rapid.sdk.exception;

import nz.net.charge.eway.rapid.sdk.util.Constant;

import java.io.Serial;

/**
 * Authentication failure - occurs when an invalid Rapid API Key or Password are
 * used and Rapid API returns a 40* HTTP status code
 */
public class AuthenticationFailureException extends RapidSdkException {

    @Serial
    private static final long serialVersionUID = -2301805600093174889L;

    public AuthenticationFailureException(String message, Throwable t) {
        super(Constant.AUTHENTICATION_FAILURE_ERROR_CODE, message, t);
    }
}
